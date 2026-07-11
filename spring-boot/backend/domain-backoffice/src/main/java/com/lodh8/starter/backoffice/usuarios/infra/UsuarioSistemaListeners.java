package com.lodh8.starter.backoffice.usuarios.infra;

import com.lodh8.starter.backoffice.usuarios.domain.event.UsuarioSistemaAtivadoEvent;
import com.lodh8.starter.backoffice.usuarios.domain.event.UsuarioSistemaCriadoEvent;
import com.lodh8.starter.backoffice.usuarios.domain.event.UsuarioSistemaInativadoEvent;
import com.lodh8.starter.notification.domain.events.NotificacaoBoasVindasEvent;
import com.lodh8.starter.notification.domain.events.NotificacaoUsuarioAtivadoEvent;
import com.lodh8.starter.notification.domain.events.NotificacaoUsuarioInativadoEvent;
import com.lodh8.starter.shared.events.app.EventPublisherPort;
import com.lodh8.starter.shared.models.enums.TipoAcesso;
import com.lodh8.starter.shared.models.enums.TipoNotificacao;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioSistemaListeners {

    private final EventPublisherPort events;

    @ApplicationModuleListener
    public void onUsuarioSistemaCriadoEvent(UsuarioSistemaCriadoEvent event) {
        var usuarioSistema = event.usuarioSistema();
        events.publishEvent(new NotificacaoBoasVindasEvent(
                usuarioSistema.nome(),
                TipoAcesso.USUARIO_SISTEMA,
                usuarioSistema.email(),
                null,
                TipoNotificacao.EMAIL
        ));
    }

    @ApplicationModuleListener
    public void onUsuarioSistemaAtivadoEvent(UsuarioSistemaAtivadoEvent event) {
        var usuarioSistema = event.usuarioSistema();
        events.publishEvent(new NotificacaoUsuarioAtivadoEvent(
                usuarioSistema.nome(),
                TipoAcesso.USUARIO_SISTEMA,
                usuarioSistema.email(),
                TipoNotificacao.EMAIL
        ));
    }

    @ApplicationModuleListener
    public void onUsuarioSistemaInativadoEvent(UsuarioSistemaInativadoEvent event) {
        var usuarioSistema = event.usuarioSistema();
        events.publishEvent(new NotificacaoUsuarioInativadoEvent(
                usuarioSistema.nome(),
                TipoAcesso.USUARIO_SISTEMA,
                usuarioSistema.email(),
                TipoNotificacao.EMAIL
        ));
    }

}
