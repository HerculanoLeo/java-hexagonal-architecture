package com.lodh8.starter.shared.exceptions;

import java.io.Serial;

public class ConflictException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -53617303624957213L;

    public ConflictException(String message) {
        super(message);
    }
}
