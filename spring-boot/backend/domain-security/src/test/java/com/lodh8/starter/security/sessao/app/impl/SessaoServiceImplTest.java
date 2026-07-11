package com.lodh8.starter.security.sessao.app.impl;

import com.lodh8.starter.identity.usuario.app.UsuarioService;
import com.lodh8.starter.identity.usuario.domain.Usuario;
import com.lodh8.starter.shared.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessaoServiceImplTest {

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private SessaoServiceImpl service;

    @Test
    void invalidateByIdentityId_success() {
        String identityId = "identity-id";
        when(usuarioService.findById(identityId)).thenReturn(Optional.of(mock(Usuario.class)));

        service.invalidateByIdentityId(identityId);

        verify(usuarioService).invalidateSessions(identityId);
    }

    @Test
    void invalidateByIdentityId_whenUserNotFound_shouldThrow() {
        String identityId = "missing-id";
        when(usuarioService.findById(identityId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.invalidateByIdentityId(identityId));
        verify(usuarioService, never()).invalidateSessions(any());
    }

}
