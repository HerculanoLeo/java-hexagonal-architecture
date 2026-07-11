package com.lodh8.starter.shared.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.herculanoleo.sentinelflow.models.ValidatorFieldErrorMessages;

import java.time.OffsetDateTime;
import java.util.Collection;

public record ServerFieldErrorsExceptionResponse(
        @JsonProperty("message") String message,
        @JsonProperty("timestamp") OffsetDateTime timestamp,
        @JsonProperty("errors") Collection<ValidatorFieldErrorMessages> fieldErrors
) {
}
