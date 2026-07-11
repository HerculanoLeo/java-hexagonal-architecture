package com.lodh8.starter.identity.usuario.app.impl;

import com.lodh8.starter.identity.usuario.app.PasswordConstraintService;
import com.lodh8.starter.identity.usuario.app.port.PasswordConstraintProviderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordConstraintServiceImpl implements PasswordConstraintService {

    private final PasswordConstraintProviderPort passwordConstraintProvider;

    @Override
    public void validate(String password) {
        passwordConstraintProvider.validate(password);
    }

    @Override
    public String generateRandomPassword() {
        return passwordConstraintProvider.generateRandomPassword();
    }
}
