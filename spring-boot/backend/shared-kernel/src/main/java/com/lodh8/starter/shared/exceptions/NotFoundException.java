package com.lodh8.starter.shared.exceptions;

import java.io.Serial;

public class NotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -880872569664048319L;

    public NotFoundException(String message) {
        super(message);
    }
}
