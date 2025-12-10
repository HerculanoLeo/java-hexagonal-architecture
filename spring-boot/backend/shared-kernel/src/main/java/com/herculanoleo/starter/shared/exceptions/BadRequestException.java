package com.herculanoleo.starter.shared.exceptions;

import java.io.Serial;

public class BadRequestException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 3663203063868018331L;

    public BadRequestException(String message) {
        super(message);
    }
}
