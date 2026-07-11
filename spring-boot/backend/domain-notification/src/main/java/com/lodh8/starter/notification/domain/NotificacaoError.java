package com.lodh8.starter.notification.domain;

import java.time.OffsetDateTime;

public record NotificacaoError(
        String message,
        OffsetDateTime timestamp
) {
}
