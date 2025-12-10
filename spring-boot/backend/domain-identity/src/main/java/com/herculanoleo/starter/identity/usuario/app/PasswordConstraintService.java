package com.herculanoleo.starter.identity.usuario.app;

public interface PasswordConstraintService {
    void validate(String password);
    String generateRandomPassword();
}
