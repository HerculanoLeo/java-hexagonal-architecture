package com.lodh8.starter.identity.usuario.infra.passy;

import com.lodh8.starter.identity.infra.attribute.PasswordConstraintAttributes;
import com.lodh8.starter.shared.exceptions.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.passay.PasswordValidator;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PasswordConstraintPassyAdapterTest {

    private PasswordValidator passwordValidator;

    private PasswordConstraintPassyAdapter adapter;

    @BeforeEach
    public void setUp() {
        var attributes = new PasswordConstraintAttributes();

        this.passwordValidator = spy(new PasswordValidator(attributes.generateRules()));

        this.adapter = new PasswordConstraintPassyAdapter(
                this.passwordValidator,
                attributes
        );
    }

    @Test
    public void test_validate_valid_password() {
        this.adapter.validate("Ab123456@");
        verify(passwordValidator, times(1)).validate(any());
    }

    @Test
    public void test_validate_invalid_password() {
        assertThrows(BadRequestException.class, () -> this.adapter.validate("1234"));
    }

    @Test
    public void test_generateRandomPassword() {
        var randomPassword = this.adapter.generateRandomPassword();
        this.adapter.validate(randomPassword);
        verify(passwordValidator, times(1)).validate(any());
    }

}