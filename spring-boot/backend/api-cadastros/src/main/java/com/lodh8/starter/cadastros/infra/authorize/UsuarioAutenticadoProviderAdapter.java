package com.lodh8.starter.cadastros.infra.authorize;

import com.lodh8.starter.authorize.app.UsuarioAutenticadoProviderService;
import com.lodh8.starter.authorize.domain.GrupoAutenticado;
import com.lodh8.starter.authorize.domain.OAuthUser;
import com.lodh8.starter.authorize.domain.UsuarioAutenticado;
import com.lodh8.starter.identity.grupos.app.GrupoService;
import com.lodh8.starter.plataforma.usuarios.app.UsuarioSistemaService;
import com.lodh8.starter.plataforma.usuarios.domain.UsuarioSistema;
import com.lodh8.starter.shared.models.enums.Status;
import com.lodh8.starter.shared.models.enums.TipoAcesso;
import com.lodh8.starter.shared.utils.IdentificadorLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioAutenticadoProviderAdapter implements UsuarioAutenticadoProviderService {

    protected static final String USER_CACHE_KEY = "authenticated-user-cache";

    private final UsuarioSistemaService usuarioSistemaService;

    private final GrupoService grupoService;

    private final CacheManager cacheManager;

    @Override
    public UsuarioAutenticado usuarioAutenticado(OAuthUser user) {
        log.info(
                "usuarioAutenticadoProvider usuarioAutenticado inicio userId={}",
                IdentificadorLog.mascarar(user != null ? user.userId() : null)
        );
        log.debug("usuarioAutenticadoProvider usuarioAutenticado inicio userId={}", user != null ? user.userId() : null);
        var cache = cacheManager.getCache(USER_CACHE_KEY);

        var cacheKey = getCacheKey(user);

        if (null != cache && StringUtils.isNotBlank(cacheKey)) {
            var authenticatedUserCache = cache.get(cacheKey, UsuarioAutenticado.class);

            if (null != authenticatedUserCache) {
                log.info("usuarioAutenticadoProvider usuarioAutenticado fim origem=cache tipo={}", authenticatedUserCache.tipo());
                return authenticatedUserCache;
            }

            var authenticatedUser = getUsuarioAutenticado(user);

            cache.put(cacheKey, authenticatedUser);

            log.info("usuarioAutenticadoProvider usuarioAutenticado fim origem=provedor tipo={}", authenticatedUser.tipo());
            return authenticatedUser;
        }

        var result = getUsuarioAutenticado(user);
        log.info("usuarioAutenticadoProvider usuarioAutenticado fim origem=provedor tipo={}", result.tipo());
        return result;
    }

    @Override
    public GrupoAutenticado grupo(OAuthUser user) {
        log.info(
                "usuarioAutenticadoProvider grupo inicio userId={}",
                IdentificadorLog.mascarar(user != null ? user.userId() : null)
        );
        log.debug("usuarioAutenticadoProvider grupo inicio userId={}", user != null ? user.userId() : null);
        var grupo = switch (user.type()) {
            case USUARIO_SISTEMA -> this.grupoService.findByIdentityId(user.userId())
                    .map(g -> new GrupoAutenticado(g.id(), g.nome(), g.roles()))
                    .orElseGet(() -> {
                        // Usuário principal sem grupo: flag main no banco local.
                        var isMain = this.usuarioSistemaService.findByIdentityId(user.userId())
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
        log.info(
                "usuarioAutenticadoProvider grupo fim resultado={}",
                grupo != null ? "ok" : "ausente"
        );
        return grupo;
    }

    protected UsuarioAutenticado getUsuarioAutenticado(OAuthUser user) {
        return switch (user.type()) {
            case USUARIO_SISTEMA -> usuarioSistemaService.findByIdentityId(user.userId())
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
        log.info(
                "usuarioAutenticadoProvider atualizarNome inicio id={}",
                IdentificadorLog.mascarar(usuarioId)
        );
        log.debug("usuarioAutenticadoProvider atualizarNome inicio id={}", usuarioId);
        usuarioSistemaService.updateNome(usuarioId, nome);

        var cache = cacheManager.getCache(USER_CACHE_KEY);
        var cacheKey = getCacheKey(oauthUser);
        if (null != cache && StringUtils.isNotBlank(cacheKey)) {
            cache.evict(cacheKey);
        }
        log.info(
                "usuarioAutenticadoProvider atualizarNome fim id={}",
                IdentificadorLog.mascarar(usuarioId)
        );
    }

    protected String getCacheKey(OAuthUser oauthUser) {
        return Optional.ofNullable(oauthUser.userId()).orElseGet(oauthUser::clientId);
    }

}