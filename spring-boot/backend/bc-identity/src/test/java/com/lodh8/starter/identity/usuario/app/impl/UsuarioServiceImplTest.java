package com.lodh8.starter.identity.usuario.app.impl;

import com.lodh8.starter.identity.usuario.app.PasswordConstraintService;
import com.lodh8.starter.identity.usuario.app.port.UsuarioProviderPort;
import com.lodh8.starter.identity.usuario.domain.*;
import com.lodh8.starter.identity.usuario.domain.events.UsuarioAtivadoEvent;
import com.lodh8.starter.identity.usuario.domain.events.UsuarioCriadoEvent;
import com.lodh8.starter.identity.usuario.domain.events.UsuarioInativadoEvent;
import com.lodh8.starter.shared.events.app.EventPublisherPort;
import com.lodh8.starter.shared.exceptions.BadRequestException;
import com.lodh8.starter.shared.exceptions.ConflictException;
import com.lodh8.starter.shared.exceptions.NotFoundException;
import com.lodh8.starter.shared.models.enums.TipoAcesso;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    private UsuarioProviderPort usuarioProvider;

    private PasswordConstraintService passwordConstraintService;

    private EventPublisherPort events;

    private UsuarioServiceImpl usuarioService;

    @BeforeEach
    public void setUp() {
        this.usuarioProvider = mock(UsuarioProviderPort.class);
        this.passwordConstraintService = mock(PasswordConstraintService.class);
        this.events = mock(EventPublisherPort.class);

        this.usuarioService = spy(new UsuarioServiceImpl(
                this.usuarioProvider,
                this.passwordConstraintService,
                this.events
        ));
    }

    @Test
    public void test_findById_should_return_usuario_when_found() {
        String userId = "test-id";
        Usuario mockUsuario = mock(Usuario.class);
        Optional<Usuario> expectedOptional = Optional.of(mockUsuario);

        when(this.usuarioProvider.findById(userId)).thenReturn(expectedOptional);

        Optional<Usuario> result = this.usuarioService.findById(userId);

        assertTrue(result.isPresent());
        assertEquals(expectedOptional, result);
        assertEquals(mockUsuario, result.get());
        verify(this.usuarioProvider, times(1)).findById(userId);
    }

    @Test
    public void test_findById_should_return_empty_when_not_found() {
        String userId = "not-found-id";
        Optional<Usuario> expectedOptional = Optional.empty();

        when(this.usuarioProvider.findById(userId)).thenReturn(expectedOptional);

        Optional<Usuario> result = this.usuarioService.findById(userId);

        assertTrue(result.isEmpty());
        assertEquals(expectedOptional, result);
        verify(this.usuarioProvider, times(1)).findById(userId);
    }

    @Test
    public void test_register_should_throw_ConflictException_when_email_exists() {
        String existingEmail = "teste@existe.com";
        UsuarioRegister mockRequest = mock(UsuarioRegister.class);
        when(mockRequest.email()).thenReturn(existingEmail);

        when(this.usuarioProvider.existsByEmail(existingEmail)).thenReturn(true);

        ConflictException exception = assertThrows(ConflictException.class, () -> {
            this.usuarioService.register(mockRequest);
        });

        assertEquals("e-mail já cadastrado", exception.getMessage());

        verify(this.usuarioProvider, times(1)).existsByEmail(existingEmail);
        verify(this.passwordConstraintService, never()).validate(any());
        verify(this.usuarioProvider, never()).register(any());
        verify(this.events, never()).publishEvent(any());
    }

    @Test
    public void test_register_should_throw_Exception_when_password_is_invalid() {
        String email = "novo@email.com";
        String invalidPassword = "123";
        UsuarioRegister mockRequest = mock(UsuarioRegister.class);
        when(mockRequest.email()).thenReturn(email);
        when(mockRequest.senha()).thenReturn(invalidPassword);

        when(this.usuarioProvider.existsByEmail(email)).thenReturn(false);
        doThrow(new BadRequestException("Senha muito curta"))
                .when(this.passwordConstraintService).validate(invalidPassword);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            this.usuarioService.register(mockRequest);
        });

        assertEquals("Senha muito curta", exception.getMessage());

        verify(this.usuarioProvider, times(1)).existsByEmail(email);
        verify(this.passwordConstraintService, times(1)).validate(invalidPassword);
        verify(this.usuarioProvider, never()).register(any());
        verify(this.events, never()).publishEvent(any());
    }

    @Test
    public void test_register_should_succeed_with_password() {
        String email = "novo@email.com";
        String validPassword = "Password123@";
        String userId = "user-123";
        TipoAcesso tipo = TipoAcesso.USUARIO_SISTEMA;
        boolean main = true;

        UsuarioRegister mockRequest = mock(UsuarioRegister.class);
        when(mockRequest.email()).thenReturn(email);
        when(mockRequest.senha()).thenReturn(validPassword);
        when(mockRequest.main()).thenReturn(main);

        Usuario mockUsuario = mock(Usuario.class);
        when(mockUsuario.id()).thenReturn(userId);
        when(mockUsuario.tipo()).thenReturn(tipo);

        when(this.usuarioProvider.existsByEmail(email)).thenReturn(false);
        doNothing().when(this.passwordConstraintService).validate(validPassword);
        when(this.usuarioProvider.register(mockRequest)).thenReturn(mockUsuario);
        doNothing().when(this.usuarioProvider).updateAdminRole(userId, tipo, main);
        doNothing().when(this.events).publishEvent(any(UsuarioCriadoEvent.class));

        Usuario result = this.usuarioService.register(mockRequest);

        assertNotNull(result);
        assertEquals(mockUsuario, result);

        verify(this.usuarioProvider, times(1)).existsByEmail(email);
        verify(this.passwordConstraintService, times(1)).validate(validPassword);
        verify(this.usuarioProvider, times(1)).register(mockRequest);
        verify(this.usuarioProvider, times(1)).updateAdminRole(userId, tipo, main);

        ArgumentCaptor<UsuarioCriadoEvent> eventCaptor = ArgumentCaptor.forClass(UsuarioCriadoEvent.class);
        verify(this.events, times(1)).publishEvent(eventCaptor.capture());
        assertEquals(mockUsuario, eventCaptor.getValue().usuario());
    }

    @Test
    public void test_register_should_succeed_with_blank_password() {
        String email = "novo@email.com";
        String userId = "user-123";
        TipoAcesso tipo = TipoAcesso.USUARIO_SISTEMA;
        boolean main = false;

        UsuarioRegister mockRequest = mock(UsuarioRegister.class);
        when(mockRequest.email()).thenReturn(email);
        when(mockRequest.senha()).thenReturn(null);
        when(mockRequest.main()).thenReturn(main);

        Usuario mockUsuario = mock(Usuario.class);
        when(mockUsuario.id()).thenReturn(userId);
        when(mockUsuario.tipo()).thenReturn(tipo);

        when(this.usuarioProvider.existsByEmail(email)).thenReturn(false);
        when(this.usuarioProvider.register(mockRequest)).thenReturn(mockUsuario);
        doNothing().when(this.usuarioProvider).updateAdminRole(userId, tipo, main);
        doNothing().when(this.events).publishEvent(any(UsuarioCriadoEvent.class));

        Usuario result = this.usuarioService.register(mockRequest);

        assertNotNull(result);

        verify(this.passwordConstraintService, never()).validate(any());

        verify(this.usuarioProvider, times(1)).existsByEmail(email);
        verify(this.usuarioProvider, times(1)).register(mockRequest);
        verify(this.usuarioProvider, times(1)).updateAdminRole(userId, tipo, main);
        verify(this.events, times(1)).publishEvent(any(UsuarioCriadoEvent.class));
    }

    @Test
    public void test_update_should_succeed_when_user_is_found() {
        String userId = "user-123";
        TipoAcesso tipo = TipoAcesso.USUARIO_SISTEMA;
        boolean main = true;

        UsuarioUpdate mockRequest = mock(UsuarioUpdate.class);
        when(mockRequest.main()).thenReturn(main);

        Usuario mockUsuario = mock(Usuario.class);
        when(mockUsuario.id()).thenReturn(userId);
        when(mockUsuario.tipo()).thenReturn(tipo);

        when(this.usuarioProvider.findById(userId)).thenReturn(Optional.of(mockUsuario));

        doNothing().when(this.usuarioProvider).update(userId, mockRequest);

        doNothing().when(this.usuarioProvider).updateAdminRole(userId, tipo, main);

        this.usuarioService.update(userId, mockRequest);

        verify(this.usuarioProvider, times(1)).findById(userId);
        verify(this.usuarioProvider, times(1)).update(userId, mockRequest);
        verify(this.usuarioProvider, times(1)).updateAdminRole(userId, tipo, main);
    }

    @Test
    public void test_update_should_throw_NotFoundException_when_user_not_found() {
        String userId = "not-found-id";
        UsuarioUpdate mockRequest = mock(UsuarioUpdate.class);

        when(this.usuarioProvider.findById(userId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            this.usuarioService.update(userId, mockRequest);
        });

        assertEquals("usuário não encontrado", exception.getMessage());

        verify(this.usuarioProvider, times(1)).findById(userId);
        verify(this.usuarioProvider, never()).update(any(), any());
        verify(this.usuarioProvider, never()).updateAdminRole(any(), any(), anyBoolean());
    }

    @Test
    public void test_ativar_should_succeed_when_user_is_inativo() {
        String userId = "user-123";

        Usuario mockUsuario = mock(Usuario.class);
        when(mockUsuario.enabled()).thenReturn(false);
        when(this.usuarioProvider.findById(userId)).thenReturn(Optional.of(mockUsuario));

        doNothing().when(this.usuarioProvider).ativar(userId);

        doNothing().when(this.events).publishEvent(any(UsuarioAtivadoEvent.class));

        this.usuarioService.ativar(userId);

        verify(this.usuarioProvider, times(1)).findById(userId);
        verify(this.usuarioProvider, times(1)).ativar(userId);

        ArgumentCaptor<UsuarioAtivadoEvent> eventCaptor = ArgumentCaptor.forClass(UsuarioAtivadoEvent.class);
        verify(this.events, times(1)).publishEvent(eventCaptor.capture());
        assertEquals(userId, eventCaptor.getValue().id());
    }

    @Test
    public void test_ativar_should_throw_NotFoundException_when_user_not_found() {
        String userId = "not-found-id";

        when(this.usuarioProvider.findById(userId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            this.usuarioService.ativar(userId);
        });

        assertEquals("usuário não encontrado", exception.getMessage());

        verify(this.usuarioProvider, never()).ativar(anyString());
        verify(this.events, never()).publishEvent(any());
    }

    @Test
    public void test_ativar_should_throw_BadRequestException_when_user_is_ja_ativo() {
        String userId = "user-123";

        Usuario mockUsuario = mock(Usuario.class);
        when(mockUsuario.enabled()).thenReturn(true);
        when(this.usuarioProvider.findById(userId)).thenReturn(Optional.of(mockUsuario));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            this.usuarioService.ativar(userId);
        });

        assertEquals("usuário já ativo", exception.getMessage());

        verify(this.usuarioProvider, times(1)).findById(userId);
        verify(this.usuarioProvider, never()).ativar(anyString());
        verify(this.events, never()).publishEvent(any());
    }

    @Test
    public void test_inativar_should_succeed_when_user_is_ativo() {
        String userId = "user-123";

        Usuario mockUsuario = mock(Usuario.class);
        when(mockUsuario.enabled()).thenReturn(true);
        when(this.usuarioProvider.findById(userId)).thenReturn(Optional.of(mockUsuario));

        doNothing().when(this.usuarioProvider).inativar(userId);

        doNothing().when(this.events).publishEvent(any(UsuarioInativadoEvent.class));

        this.usuarioService.inativar(userId);

        verify(this.usuarioProvider, times(1)).findById(userId);
        verify(this.usuarioProvider, times(1)).inativar(userId);

        ArgumentCaptor<UsuarioInativadoEvent> eventCaptor = ArgumentCaptor.forClass(UsuarioInativadoEvent.class);
        verify(this.events, times(1)).publishEvent(eventCaptor.capture());
        assertEquals(userId, eventCaptor.getValue().id());
    }

    @Test
    public void test_inativar_should_throw_NotFoundException_when_user_not_found() {
        String userId = "not-found-id";

        when(this.usuarioProvider.findById(userId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            this.usuarioService.inativar(userId);
        });

        assertEquals("usuário não encontrado", exception.getMessage());
        verify(this.usuarioProvider, never()).inativar(anyString());
        verify(this.events, never()).publishEvent(any());
    }

    @Test
    public void test_inativar_should_throw_BadRequestException_when_user_is_ja_inativo() {
        String userId = "user-123";

        Usuario mockUsuario = mock(Usuario.class);
        when(mockUsuario.enabled()).thenReturn(false);
        when(this.usuarioProvider.findById(userId)).thenReturn(Optional.of(mockUsuario));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            this.usuarioService.inativar(userId);
        });

        assertEquals("usuário já inativo", exception.getMessage());
        verify(this.usuarioProvider, times(1)).findById(userId);
        verify(this.usuarioProvider, never()).inativar(anyString());
        verify(this.events, never()).publishEvent(any());
    }

    @Test
    public void test_delete_should_succeed_when_user_is_found() {
        String userId = "user-123";

        Usuario mockUsuario = mock(Usuario.class);
        when(this.usuarioProvider.findById(userId)).thenReturn(Optional.of(mockUsuario));

        doNothing().when(this.usuarioProvider).delete(userId);

        this.usuarioService.delete(userId);

        verify(this.usuarioProvider, times(1)).findById(userId);
        verify(this.usuarioProvider, times(1)).delete(userId);
    }

    @Test
    public void test_delete_should_throw_NotFoundException_when_user_not_found() {
        String userId = "not-found-id";

        when(this.usuarioProvider.findById(userId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            this.usuarioService.delete(userId);
        });

        assertEquals("usuário não encontrado", exception.getMessage());

        verify(this.usuarioProvider, times(1)).findById(userId);
        verify(this.usuarioProvider, never()).delete(anyString());
    }

    @Test
    public void test_resetPassword_should_succeed_when_user_is_found() {
        String userId = "user-123";
        RedirectAction mockRequest = mock(RedirectAction.class);

        Usuario mockUsuario = mock(Usuario.class);
        when(this.usuarioProvider.findById(userId)).thenReturn(Optional.of(mockUsuario));

        doNothing().when(this.usuarioProvider).resetPassword(userId, mockRequest);

        this.usuarioService.resetPassword(userId, mockRequest);

        verify(this.usuarioProvider, times(1)).findById(userId);
        verify(this.usuarioProvider, times(1)).resetPassword(userId, mockRequest);
    }

    @Test
    public void test_resetPassword_should_throw_NotFoundException_when_user_not_found() {
        String userId = "not-found-id";
        RedirectAction mockRequest = mock(RedirectAction.class);

        when(this.usuarioProvider.findById(userId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            this.usuarioService.resetPassword(userId, mockRequest);
        });

        assertEquals("usuário não encontrado", exception.getMessage());

        verify(this.usuarioProvider, times(1)).findById(userId);
        verify(this.usuarioProvider, never()).resetPassword(anyString(), any(RedirectAction.class));
    }

    @Test
    public void test_changePassword_should_throw_BadRequestException_when_senhas_is_null() {
        String userId = "user-123";
        TrocaSenha mockRequest = mock(TrocaSenha.class);
        when(mockRequest.novaSenha()).thenReturn(null);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            this.usuarioService.changePassword(userId, mockRequest);
        });

        assertEquals("Senha e Confirma Senha não são iguais", exception.getMessage());

        verify(this.passwordConstraintService, never()).validate(any());
        verify(this.usuarioProvider, never()).findById(any());
        verify(this.usuarioProvider, never()).changePassword(any(), any());
    }

    @Test
    public void test_changePassword_should_throw_BadRequestException_when_senhas_do_not_match() {
        String userId = "user-123";
        TrocaSenha mockRequest = mock(TrocaSenha.class);
        when(mockRequest.novaSenha()).thenReturn("Senha123");
        when(mockRequest.confirmacaoSenha()).thenReturn("SenhaDiferente456");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            this.usuarioService.changePassword(userId, mockRequest);
        });

        assertEquals("Senha e Confirma Senha não são iguais", exception.getMessage());

        verify(this.passwordConstraintService, never()).validate(any());
        verify(this.usuarioProvider, never()).findById(any());
        verify(this.usuarioProvider, never()).changePassword(any(), any());
    }

    @Test
    public void test_changePassword_should_throw_BadRequestException_when_password_is_invalid() {
        String userId = "user-123";
        String invalidPass = "123";
        TrocaSenha mockRequest = mock(TrocaSenha.class);
        when(mockRequest.novaSenha()).thenReturn(invalidPass);
        when(mockRequest.confirmacaoSenha()).thenReturn(invalidPass);

        doThrow(new BadRequestException("Senha muito fraca"))
                .when(this.passwordConstraintService).validate(invalidPass);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            this.usuarioService.changePassword(userId, mockRequest);
        });

        assertEquals("Senha muito fraca", exception.getMessage());

        verify(this.passwordConstraintService, times(1)).validate(invalidPass);
        verify(this.usuarioProvider, never()).findById(any());
        verify(this.usuarioProvider, never()).changePassword(any(), any());
    }

    @Test
    public void test_changePassword_should_throw_NotFoundException_when_user_not_found() {
        String userId = "not-found-id";
        String validPass = "SenhaForte123@";
        TrocaSenha mockRequest = mock(TrocaSenha.class);
        when(mockRequest.novaSenha()).thenReturn(validPass);
        when(mockRequest.confirmacaoSenha()).thenReturn(validPass);

        doNothing().when(this.passwordConstraintService).validate(validPass);

        when(this.usuarioProvider.findById(userId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            this.usuarioService.changePassword(userId, mockRequest);
        });

        assertEquals("usuário não encontrado", exception.getMessage());

        verify(this.passwordConstraintService, times(1)).validate(validPass);
        verify(this.usuarioProvider, times(1)).findById(userId);
        verify(this.usuarioProvider, never()).changePassword(any(), any());
    }

    @Test
    public void test_changePassword_should_succeed_and_invalidate_sessions() {
        String userId = "user-123";
        String validPass = "SenhaForte123@";
        TrocaSenha mockRequest = mock(TrocaSenha.class);
        when(mockRequest.novaSenha()).thenReturn(validPass);
        when(mockRequest.confirmacaoSenha()).thenReturn(validPass);

        doNothing().when(this.passwordConstraintService).validate(validPass);

        Usuario mockUsuario = mock(Usuario.class);
        when(this.usuarioProvider.findById(userId)).thenReturn(Optional.of(mockUsuario));

        doNothing().when(this.usuarioProvider).changePassword(userId, mockRequest);

        doNothing().when(this.usuarioService).invalidateSessions(userId);
        when(this.usuarioService.findById(userId)).thenReturn(Optional.of(mockUsuario));

        this.usuarioService.changePassword(userId, mockRequest);

        verify(this.passwordConstraintService, times(1)).validate(validPass);
        verify(this.usuarioService, times(1)).findById(userId);
        verify(this.usuarioProvider, times(1)).changePassword(userId, mockRequest);

        verify(this.usuarioService, times(1)).invalidateSessions(userId);
    }

    @Test
    public void test_invalidateSessions_should_call_provider() {
        String userId = "user-123";
        doNothing().when(this.usuarioProvider).invalidateSessions(userId);
        this.usuarioService.invalidateSessions(userId);
        verify(this.usuarioProvider, times(1)).invalidateSessions(userId);
    }

}