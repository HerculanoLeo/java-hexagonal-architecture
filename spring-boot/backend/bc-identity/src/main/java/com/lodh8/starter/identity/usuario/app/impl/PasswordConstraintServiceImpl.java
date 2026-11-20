package com.lodh8.starter.identity.usuario.app.impl;

import com.lodh8.starter.identity.usuario.app.PasswordConstraintService;
import com.lodh8.starter.identity.usuario.app.port.PasswordConstraintProviderPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordConstraintServiceImpl implements PasswordConstraintService {

    private final PasswordConstraintProviderPort passwordConstraintProvider;

    @Override
    public void validate(String password) {
        log.info("passwordConstraint validate inicio");
        passwordConstraintProvider.validate(password);
        log.info("passwordConstraint validate fim resultado=ok");
    }

    @Override
    public String generateRandomPassword() {
        log.info("passwordConstraint generateRandomPassword inicio");
        var generated = passwordConstraintProvider.generateRandomPassword();
        log.info("passwordConstraint generateRandomPassword fim resultado=ok");
        return generated;
    }
}
