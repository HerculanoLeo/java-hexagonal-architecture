package com.lodh8.starter.cadastros.infra.configuration;

import com.herculanoleo.sentinelflow.exceptions.ValidatorException;
import com.lodh8.starter.shared.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;
import java.util.NoSuchElementException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
@Slf4j
public class CommonExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({NotFoundException.class})
    protected ResponseEntity<ServerExceptionResponse> notFound(NotFoundException ex) {
        return buildResponseEntity(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler({NoSuchElementException.class})
    protected ResponseEntity<ServerExceptionResponse> notFound(NoSuchElementException ex) {
        return buildResponseEntity(HttpStatus.NOT_FOUND, "registro não encontrado.");
    }

    @ExceptionHandler({BadRequestException.class})
    protected ResponseEntity<ServerExceptionResponse> badRequest(BadRequestException ex) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler({ConflictException.class})
    protected ResponseEntity<ServerExceptionResponse> conflict(ConflictException ex) {
        return buildResponseEntity(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler({ValidatorException.class})
    protected ResponseEntity<ServerFieldErrorsExceptionResponse> validator(ValidatorException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ServerFieldErrorsExceptionResponse(ex.getMessage(), OffsetDateTime.now(), ex.getFieldErrors()));
    }

    @ExceptionHandler({AccessDeniedException.class})
    protected ResponseEntity<ServerExceptionResponse> accessDenied(Exception ex) {
        return this.buildResponseEntity(HttpStatus.FORBIDDEN, ex.getLocalizedMessage());
    }

    /**
     * Catch-all. Also unwraps {@link ValidatorException} when AOP/proxy wraps the checked exception
     * (Spring only walks the cause chain when no handler matches the outer type — and {@code Exception}
     * always matches this method).
     */
    @ExceptionHandler({Exception.class})
    protected ResponseEntity<?> handleUnexpected(Exception ex) {
        ValidatorException validatorException = findCause(ex, ValidatorException.class);
        if (validatorException != null) {
            return validator(validatorException);
        }

        log.error("ocorreu um erro inesperado", ex);
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro inesperado, contate o Administrador.");
    }

    private ResponseEntity<ServerExceptionResponse> buildResponseEntity(HttpStatus httpStatus, String message) {
        return ResponseEntity
                .status(httpStatus)
                .body(new ServerExceptionResponse(message, OffsetDateTime.now()));
    }

    private static <T extends Throwable> T findCause(Throwable throwable, Class<T> type) {
        Throwable current = throwable;
        while (current != null) {
            if (type.isInstance(current)) {
                return type.cast(current);
            }
            current = current.getCause();
        }
        return null;
    }

}
