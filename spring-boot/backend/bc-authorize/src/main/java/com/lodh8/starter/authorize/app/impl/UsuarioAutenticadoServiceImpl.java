package com.lodh8.starter.authorize.app.impl;

import com.lodh8.starter.authorize.app.UsuarioAutenticadoProviderService;
import com.lodh8.starter.authorize.app.UsuarioAutenticadoService;
import com.lodh8.starter.authorize.app.port.OauthUserProviderPort;
import com.lodh8.starter.authorize.domain.GrupoAutenticado;
import com.lodh8.starter.authorize.domain.OAuthUser;
import com.lodh8.starter.authorize.domain.UsuarioAutenticado;
import com.lodh8.starter.identity.usuario.app.UsuarioService;
import com.lodh8.starter.identity.usuario.domain.TrocaSenha;
import com.lodh8.starter.notificacao.domain.events.NotificacaoTrocaSenhaEvent;
import com.lodh8.starter.shared.events.app.EventPublisherPort;
import com.lodh8.starter.shared.exceptions.BadRequestException;
import com.lodh8.starter.shared.models.enums.TipoAcesso;
import com.lodh8.starter.shared.models.enums.TipoNotificacao;
import com.lodh8.starter.shared.utils.IdentificadorLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioAutenticadoServiceImpl implements UsuarioAutenticadoService {

    private final OauthUserProviderPort oauthUserProviderPort;

    private final UsuarioAutenticadoProviderService usuarioAutenticadoProviderPort;

    private final UsuarioService usuarioService;

    private final EventPublisherPort events;

    @Override
    public OAuthUser oauthUser() {
        log.info("usuarioAutenticado oauthUser inicio");
        var user = oauthUserProviderPort.oauthUser();
        log.debug("usuarioAutenticado oauthUser fim userId={} tipo={}", user != null ? user.userId() : null, user != null ? user.type() : null);
        log.info("usuarioAutenticado oauthUser fim tipo={}", user != null ? user.type() : null);
        return user;
    }

    @Override
    public UsuarioAutenticado usuarioAutenticado() {
        log.info("usuarioAutenticado usuarioAutenticado inicio");
        var user = oauthUser();
        var result = usuarioAutenticadoProviderPort.usuarioAutenticado(user);
        log.debug("usuarioAutenticado usuarioAutenticado fim identityId={} tipo={}", result.identityId(), result.tipo());
        log.info(
                "usuarioAutenticado usuarioAutenticado fim tipo={}",
                result.tipo()
        );
        return result;
    }

    @Override
    public GrupoAutenticado grupoAutenticado() {
        var user = oauthUserProviderPort.oauthUser();
        log.info("usuarioAutenticado grupoAutenticado inicio userId={}", IdentificadorLog.mascarar(user != null ? user.userId() : null));
        log.debug("usuarioAutenticado grupoAutenticado inicio userId={}", user != null ? user.userId() : null);
        var grupo = usuarioAutenticadoProviderPort.grupo(user);
        log.info(
                "usuarioAutenticado grupoAutenticado fim resultado={}",
                grupo != null ? "ok" : "ausente"
        );
        return grupo;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public UsuarioAutenticado updateMe(String nome) {
        var oauthUser = oauthUserProviderPort.oauthUser();
        log.info(
                "usuarioAutenticado updateMe inicio userId={}",
                IdentificadorLog.mascarar(oauthUser != null ? oauthUser.userId() : null)
        );
        log.debug("usuarioAutenticado updateMe inicio userId={}", oauthUser != null ? oauthUser.userId() : null);
        var me = usuarioAutenticadoProviderPort.usuarioAutenticado(oauthUser);

        if (!TipoAcesso.USUARIO_SISTEMA.equals(me.tipo()) || me.id() == null || me.identityId() == null) {
            log.warn("usuarioAutenticado updateMe resultado=indisponivel tipo={}", me.tipo());
            throw new BadRequestException("atualização de perfil disponível apenas para usuários do sistema");
        }

        usuarioService.updateNome(me.identityId(), nome);
        usuarioAutenticadoProviderPort.atualizarNome(me.id(), nome, oauthUser);

        var atualizado = usuarioAutenticadoProviderPort.usuarioAutenticado(oauthUser);
        log.info("usuarioAutenticado updateMe fim id={}", IdentificadorLog.mascarar(me.id().toString()));
        log.debug("usuarioAutenticado updateMe fim id={}", me.id());
        return atualizado;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void changePassword(TrocaSenha trocaSenha) {
        var me = usuarioAutenticado();
        log.info(
                "usuarioAutenticado changePassword inicio identityId={}",
                IdentificadorLog.mascarar(me.identityId())
        );
        log.debug("usuarioAutenticado changePassword inicio identityId={}", me.identityId());

        if (!TipoAcesso.USUARIO_SISTEMA.equals(me.tipo()) || me.identityId() == null) {
            log.warn("usuarioAutenticado changePassword resultado=indisponivel tipo={}", me.tipo());
            throw new BadRequestException("troca de senha disponível apenas para usuários do sistema");
        }

        usuarioService.changePassword(me.identityId(), trocaSenha);

        events.publishEvent(new NotificacaoTrocaSenhaEvent(
                me.nome(),
                me.tipo(),
                me.email(),
                trocaSenha.novaSenha(),
                TipoNotificacao.EMAIL
        ));
        log.info(
                "usuarioAutenticado changePassword fim identityId={}",
                IdentificadorLog.mascarar(me.identityId())
        );
        log.debug("usuarioAutenticado changePassword fim identityId={}", me.identityId());
    }

}