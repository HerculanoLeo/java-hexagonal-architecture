package com.lodh8.starter.notificacao.domain.events;

import com.lodh8.starter.notificacao.domain.Notificacao;

public record NotificacaoRegisterEvent(
        Notificacao notificacao
) {
}
