package com.lodh8.starter.cadastros.infra.authorize;

import com.lodh8.starter.authorize.app.port.UsuarioAutenticadoProviderPort;
import com.lodh8.starter.authorize.domain.GrupoAutenticado;
import com.lodh8.starter.authorize.domain.OAuthUser;
import com.lodh8.starter.authorize.domain.UsuarioAutenticado;
import com.lodh8.starter.backoffice.usuarios.app.port.UsuarioSistemaRepositoryPort;
import com.lodh8.starter.backoffice.usuarios.domain.UsuarioSistema;
import com.lodh8.starter.identity.grupos.app.GrupoService;
import com.lodh8.starter.shared.models.enums.Status;
import com.lodh8.starter.shared.models.enums.TipoAcesso;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

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
            case USUARIO_SISTEMA -> this.grupoService.findByIdentityId(user.userId())
                    .map(grupo -> new GrupoAutenticado(grupo.id(), grupo.nome(), grupo.roles()))
                    .orElseGet(() -> {
                        // Usuário principal sem grupo: flag main no banco local.
                        var isMain = this.usuarioSistemaRepository.findByIdentityId(user.userId())
                                .map(UsuarioSistema::main)
                                .orElse(false);
                        if (isMain) {
                            return new GrupoAutenticado(
                                    "main",
                                    "Usuário principal",
                                    user.roles()
                            );
                        }
                        return null;
                    });
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

    @Override
    public void atualizarNome(UUID usuarioId, String nome, OAuthUser oauthUser) {
        usuarioSistemaRepository.updateNome(usuarioId, nome);

        var cache = cacheManager.getCache(USER_CACHE_KEY);
        var cacheKey = getCacheKey(oauthUser);
        if (null != cache && StringUtils.isNotBlank(cacheKey)) {
            cache.evict(cacheKey);
        }
    }

    protected String getCacheKey(OAuthUser oauthUser) {
        return Optional.ofNullable(oauthUser.userId()).orElseGet(oauthUser::clientId);
    }

}
