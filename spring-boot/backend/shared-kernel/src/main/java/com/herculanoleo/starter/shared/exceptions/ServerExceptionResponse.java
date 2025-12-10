package com.herculanoleo.starter.shared.exceptions;

import java.time.OffsetDateTime;

public record ServerExceptionResponse(String message, OffsetDateTime timestamp) {
}
