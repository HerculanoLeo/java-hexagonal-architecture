package com.lodh8.starter.identity.usuario.app;

public interface PasswordConstraintService {
    void validate(String password);
    String generateRandomPassword();
}
