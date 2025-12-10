package com.herculanoleo.starter.notification.domain.events;

import com.herculanoleo.starter.notification.domain.Notificacao;

public record NotificacaoRegisterEvent(
        Notificacao notificacao
) {
}
