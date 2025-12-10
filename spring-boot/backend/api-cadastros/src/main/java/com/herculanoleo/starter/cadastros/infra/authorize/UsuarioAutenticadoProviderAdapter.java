package com.herculanoleo.starter.cadastros.infra.authorize;

import com.herculanoleo.starter.authorize.app.port.UsuarioAutenticadoProviderPort;
import com.herculanoleo.starter.authorize.domain.GrupoAutenticado;
import com.herculanoleo.starter.authorize.domain.OAuthUser;
import com.herculanoleo.starter.authorize.domain.UsuarioAutenticado;
import com.herculanoleo.starter.identity.grupos.app.GrupoService;
import com.herculanoleo.starter.plataformadmin.usuarios.app.port.UsuarioSistemaRepositoryPort;
import com.herculanoleo.starter.shared.models.enums.Status;
import com.herculanoleo.starter.shared.models.enums.TipoAcesso;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioAutenticadoProviderAdapter implements UsuarioAutenticadoProviderPort {

    protected static final String USER_CACHE_KEY = "authenticated-user-cache";

    private final UsuarioSistemaRepositoryPort usuarioSistemaRepository;

    private final GrupoService grupoService;

    private final CacheManager cacheManager;

    @Override
    public UsuarioAutenticado usuarioAutenticado(OAuthUser user) {
        var cache = cacheManager.getCache(USER_CACHE_KEY);

        var cacheKey = getCacheKey(user);

        if (null != cache && StringUtils.isNotBlank(cacheKey)) {
            var authenticatedUserCache = cache.get(cacheKey, UsuarioAutenticado.class);

            if (null != authenticatedUserCache) {
                return authenticatedUserCache;
            }

            var authenticatedUser = getUsuarioAutenticado(user);

            cache.put(cacheKey, authenticatedUser);

            return authenticatedUser;
        }

        return getUsuarioAutenticado(user);
    }

    @Override
    public GrupoAutenticado grupo(OAuthUser user) {
        return switch (user.type()) {
            case USUARIO_SISTEMA -> {
                var grupo = this.grupoService.findByIdentityId(user.userId()).orElseThrow();
                yield new GrupoAutenticado(grupo.id(), grupo.nome(), grupo.roles());
            }
            case null, default -> null;
        };
    }

    protected UsuarioAutenticado getUsuarioAutenticado(OAuthUser user) {
        return switch (user.type()) {
            case USUARIO_SISTEMA -> usuarioSistemaRepository.findByIdentityId(user.userId())
                    .map(usuario -> new UsuarioAutenticado(
                            usuario.id(),
                            usuario.identityId(),
                            null,
                            usuario.nome(),
                            usuario.email(),
                            user.type(),
                            usuario.status(),
                            usuario.versao()
                    )).orElseThrow();
            case CLIENTE_SISTEMA -> new UsuarioAutenticado(
                    null,
                    user.userId(),
                    user.details().relacionadoId(),
                    null,
                    null,
                    user.type(),
                    Status.ATIVO,
                    0
            );
            default -> new UsuarioAutenticado(TipoAcesso.ANONYMOUS);
        };
    }

    protected String getCacheKey(OAuthUser oauthUser) {
        return Optional.ofNullable(oauthUser.userId()).orElseGet(oauthUser::clientId);
    }

}
