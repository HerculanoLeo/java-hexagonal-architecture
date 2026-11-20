package com.lodh8.starter.notificacao.domain;

import java.time.OffsetDateTime;

public record NotificacaoError(
        String message,
        OffsetDateTime timestamp
) {
}
