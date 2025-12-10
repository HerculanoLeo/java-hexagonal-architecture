package com.herculanoleo.starter.identity.usuario.app.port;

public interface PasswordConstraintProviderPort {
    void validate(String password);
    String generateRandomPassword();
}
