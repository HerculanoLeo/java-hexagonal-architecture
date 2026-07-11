package com.lodh8.starter.backoffice.usuarios.app;

import com.herculanoleo.sentinelflow.exceptions.ValidatorException;
import com.herculanoleo.sentinelflow.validations.impl.ValidationFactoryImpl;
import com.herculanoleo.sentinelflow.validator.impl.ValidatorFactoryImpl;
import com.lodh8.starter.backoffice.usuarios.domain.UsuarioSistemaRegister;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UsuarioSistemaRegisterValidatorTest {

    private UsuarioSistemaRegisterValidator validator;

    @BeforeEach
    void setUp() {
        validator = new UsuarioSistemaRegisterValidator(
                new ValidatorFactoryImpl(),
                new ValidationFactoryImpl()
        );
    }

    @Test
    void validate_withValidRegister_shouldPass() {
        var register = new UsuarioSistemaRegister("João Silva", "joao@test.com", null, "group-id");

        assertDoesNotThrow(() -> validator.validate(register));
    }

    @Test
    void validate_withInvalidEmail_shouldThrow() {
        var register = new UsuarioSistemaRegister("João Silva", "invalid", null, "group-id");

        assertThrows(ValidatorException.class, () -> validator.validate(register));
    }

}
