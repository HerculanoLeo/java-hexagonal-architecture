package com.lodh8.starter.authorize.app.impl;

import com.lodh8.starter.authorize.app.UsuarioAutenticadoService;
import com.lodh8.starter.authorize.app.port.OauthUserProviderPort;
import com.lodh8.starter.authorize.app.port.UsuarioAutenticadoProviderPort;
import com.lodh8.starter.authorize.domain.GrupoAutenticado;
import com.lodh8.starter.authorize.domain.UsuarioAutenticado;
import com.lodh8.starter.identity.usuario.app.UsuarioService;
import com.lodh8.starter.identity.usuario.domain.TrocaSenha;
import com.lodh8.starter.notification.domain.events.NotificacaoTrocaSenhaEvent;
import com.lodh8.starter.shared.events.app.EventPublisherPort;
import com.lodh8.starter.shared.exceptions.BadRequestException;
import com.lodh8.starter.shared.models.enums.TipoAcesso;
import com.lodh8.starter.shared.models.enums.TipoNotificacao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioAutenticadoServiceImpl implements UsuarioAutenticadoService {

    private final OauthUserProviderPort oauthUserProviderPort;

    private final UsuarioAutenticadoProviderPort usuarioAutenticadoProviderPort;

    private final UsuarioService usuarioService;

    private final EventPublisherPort events;

    @Override
    public UsuarioAutenticado usuarioAutenticado() {
        var user = oauthUserProviderPort.oauthUser();
        return usuarioAutenticadoProviderPort.usuarioAutenticado(user);
    }

    @Override
    public GrupoAutenticado grupoAutenticado() {
        var user = oauthUserProviderPort.oauthUser();
        return usuarioAutenticadoProviderPort.grupo(user);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public UsuarioAutenticado updateMe(String nome) {
        var oauthUser = oauthUserProviderPort.oauthUser();
        var me = usuarioAutenticadoProviderPort.usuarioAutenticado(oauthUser);

        if (!TipoAcesso.USUARIO_SISTEMA.equals(me.tipo()) || me.id() == null || me.identityId() == null) {
            throw new BadRequestException("atualização de perfil disponível apenas para usuários do sistema");
        }

        usuarioService.updateNome(me.identityId(), nome);
        usuarioAutenticadoProviderPort.atualizarNome(me.id(), nome, oauthUser);

        return usuarioAutenticadoProviderPort.usuarioAutenticado(oauthUser);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void changePassword(TrocaSenha trocaSenha) {
        var me = usuarioAutenticado();

        if (!TipoAcesso.USUARIO_SISTEMA.equals(me.tipo()) || me.identityId() == null) {
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
    }

}
