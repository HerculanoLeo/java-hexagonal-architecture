package com.herculanoleo.starter.notification.domain;

import java.time.OffsetDateTime;

public record NotificacaoError(
        String message,
        OffsetDateTime timestamp
) {
}
