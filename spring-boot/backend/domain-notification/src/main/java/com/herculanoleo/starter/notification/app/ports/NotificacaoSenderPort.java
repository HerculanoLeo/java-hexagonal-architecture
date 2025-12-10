package com.herculanoleo.starter.notification.app.ports;

import com.herculanoleo.starter.notification.domain.Notificacao;
import com.herculanoleo.starter.shared.models.enums.TipoNotificacao;

public interface NotificacaoSenderPort {

    void enviar(Notificacao requestEntity);

    boolean suporta(TipoNotificacao tipo);

}
