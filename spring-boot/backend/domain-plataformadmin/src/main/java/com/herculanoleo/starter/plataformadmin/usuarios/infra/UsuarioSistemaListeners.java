package com.herculanoleo.starter.plataformadmin.usuarios.infra;

import com.herculanoleo.starter.notification.domain.events.NotificacaoBoasVindasEvent;
import com.herculanoleo.starter.plataformadmin.usuarios.domain.event.UsuarioSistemaCriadoEvent;
import com.herculanoleo.starter.shared.events.app.EventPublisherPort;
import com.herculanoleo.starter.shared.models.enums.TipoAcesso;
import com.herculanoleo.starter.shared.models.enums.TipoNotificacao;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;

//TODO sugestão: criar notificações para os eventos ativar e inativar o usuário
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

}
