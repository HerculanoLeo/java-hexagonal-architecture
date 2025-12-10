package com.herculanoleo.starter.shared.exceptions;

import com.herculanoleo.sentinelflow.models.ValidatorFieldErrorMessages;

import java.time.OffsetDateTime;
import java.util.Collection;

public record ServerFieldErrorsExceptionResponse(
        String message,
        OffsetDateTime timestamp,
        Collection<ValidatorFieldErrorMessages> fieldErrors
) {
}
