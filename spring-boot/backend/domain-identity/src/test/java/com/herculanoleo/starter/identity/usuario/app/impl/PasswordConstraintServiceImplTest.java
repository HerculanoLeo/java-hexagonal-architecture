package com.herculanoleo.starter.identity.usuario.app.impl;

import com.herculanoleo.starter.identity.usuario.app.port.PasswordConstraintProviderPort;
import com.herculanoleo.starter.shared.exceptions.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PasswordConstraintServiceImplTest {

    private PasswordConstraintProviderPort passwordConstraintProvider;

    private PasswordConstraintServiceImpl service;

    @BeforeEach
    public void setUp() {
        this.passwordConstraintProvider = mock(PasswordConstraintProviderPort.class);

        this.service = new PasswordConstraintServiceImpl(this.passwordConstraintProvider);
    }

    @Test
    public void test_validate_should_delegate_to_provider() {
        String password = "TestPassword123@";

        doNothing().when(this.passwordConstraintProvider).validate(password);

        this.service.validate(password);

        verify(this.passwordConstraintProvider, times(1)).validate(password);
    }

    @Test
    public void test_validate_should_propagate_exception_from_provider() {
        String invalidPassword = "123";

        doThrow(new BadRequestException("Senha muito curta"))
                .when(this.passwordConstraintProvider).validate(invalidPassword);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            this.service.validate(invalidPassword);
        });

        assertEquals("Senha muito curta", exception.getMessage());

        verify(this.passwordConstraintProvider, times(1)).validate(invalidPassword);
    }

    @Test
    public void test_generateRandomPassword_should_delegate_to_provider_and_return_result() {
        String expectedPassword = "R@nd0mP@ssw0rd!";

        when(this.passwordConstraintProvider.generateRandomPassword()).thenReturn(expectedPassword);

        String actualPassword = this.service.generateRandomPassword();

        assertEquals(expectedPassword, actualPassword);

        verify(this.passwordConstraintProvider, times(1)).generateRandomPassword();
    }
}