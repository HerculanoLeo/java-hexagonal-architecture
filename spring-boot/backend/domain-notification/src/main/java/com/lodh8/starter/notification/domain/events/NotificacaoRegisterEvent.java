package com.lodh8.starter.notification.domain.events;

import com.lodh8.starter.notification.domain.Notificacao;

public record NotificacaoRegisterEvent(
        Notificacao notificacao
) {
}
