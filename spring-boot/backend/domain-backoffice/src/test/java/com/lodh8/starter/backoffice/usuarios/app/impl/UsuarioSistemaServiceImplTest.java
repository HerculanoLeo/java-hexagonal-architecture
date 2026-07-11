package com.lodh8.starter.backoffice.usuarios.app.impl;

import com.lodh8.starter.backoffice.grupos.app.GrupoSistemaService;
import com.lodh8.starter.backoffice.grupos.domain.GrupoSistema;
import com.lodh8.starter.backoffice.usuarios.app.UsuarioSistemaRegisterValidator;
import com.lodh8.starter.backoffice.usuarios.app.UsuarioSistemaUpdateValidator;
import com.lodh8.starter.backoffice.usuarios.app.port.SistemaRedirectActionPort;
import com.lodh8.starter.backoffice.usuarios.app.port.UsuarioSistemaRepositoryPort;
import com.lodh8.starter.backoffice.usuarios.domain.UsuarioSistema;
import com.lodh8.starter.backoffice.usuarios.domain.UsuarioSistemaGrupo;
import com.lodh8.starter.backoffice.usuarios.domain.UsuarioSistemaRegister;
import com.lodh8.starter.backoffice.usuarios.domain.UsuarioSistemaUpdate;
import com.lodh8.starter.backoffice.usuarios.domain.event.UsuarioSistemaAtivadoEvent;
import com.lodh8.starter.backoffice.usuarios.domain.event.UsuarioSistemaAtualizadoEvent;
import com.lodh8.starter.backoffice.usuarios.domain.event.UsuarioSistemaCriadoEvent;
import com.lodh8.starter.backoffice.usuarios.domain.event.UsuarioSistemaInativadoEvent;
import com.lodh8.starter.identity.usuario.app.UsuarioService;
import com.lodh8.starter.identity.usuario.domain.Usuario;
import com.lodh8.starter.identity.usuario.domain.UsuarioRegister;
import com.lodh8.starter.identity.usuario.domain.UsuarioUpdate;
import com.lodh8.starter.shared.events.app.EventPublisherPort;
import com.lodh8.starter.shared.exceptions.ConflictException;
import com.lodh8.starter.shared.exceptions.NotFoundException;
import com.lodh8.starter.shared.models.enums.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioSistemaServiceImplTest {

    @Mock
    private UsuarioService usuarioService;
    @Mock
    private GrupoSistemaService grupoSistemaService;
    @Mock
    private SistemaRedirectActionPort redirectActionPort;
    @Mock
    private UsuarioSistemaRepositoryPort repository;
    @Mock
    private EventPublisherPort events;
    @Mock
    private UsuarioSistemaRegisterValidator registerValidator;
    @Mock
    private UsuarioSistemaUpdateValidator updateValidator;

    @InjectMocks
    private UsuarioSistemaServiceImpl service;

    @Test
    void findByIdentityId_shouldDelegateToRepository() {
        String identityId = "identity-123";
        when(repository.findByIdentityId(identityId)).thenReturn(Optional.of(mock(UsuarioSistema.class)));
        assertTrue(service.findByIdentityId(identityId).isPresent());
        verify(repository).findByIdentityId(identityId);
    }

    @Test
    void findGrupoById_whenUserAndGroupExist_shouldReturnGrupo() {
        UUID userId = UUID.randomUUID();
        String identityId = "identity-123";
        var user = mock(UsuarioSistema.class);
        var group = mock(GrupoSistema.class);

        when(user.identityId()).thenReturn(identityId);
        when(group.id()).thenReturn("group-id");
        when(group.nome()).thenReturn("Group Name");

        when(repository.findById(userId)).thenReturn(Optional.of(user));
        when(grupoSistemaService.findByIdentityId(identityId)).thenReturn(Optional.of(group));

        Optional<UsuarioSistemaGrupo> result = service.findGrupoById(userId);

        assertTrue(result.isPresent());
        assertEquals("group-id", result.get().id());
        assertEquals("Group Name", result.get().nome());
    }

    @Test
    void findGrupoById_whenUserNotFound_shouldReturnEmpty() {
        UUID userId = UUID.randomUUID();
        when(repository.findById(userId)).thenReturn(Optional.empty());
        Optional<UsuarioSistemaGrupo> result = service.findGrupoById(userId);
        assertTrue(result.isEmpty());
        verify(grupoSistemaService, never()).findByIdentityId(any());
    }

    @Test
    void findGrupoById_whenGroupNotFound_shouldReturnEmpty() {
        UUID userId = UUID.randomUUID();
        String identityId = "identity-123";
        var user = mock(UsuarioSistema.class);
        when(user.identityId()).thenReturn(identityId);

        when(repository.findById(userId)).thenReturn(Optional.of(user));
        when(grupoSistemaService.findByIdentityId(identityId)).thenReturn(Optional.empty());

        Optional<UsuarioSistemaGrupo> result = service.findGrupoById(userId);
        assertTrue(result.isEmpty());
    }

    @Test
    void register_whenEmailExists_shouldThrowConflictException() {
        var request = new UsuarioSistemaRegister("Test", "test@test.com", null, "group-id");
        when(repository.findByEmail("test@test.com")).thenReturn(Optional.of(mock(UsuarioSistema.class)));
        assertThrows(ConflictException.class, () -> service.register(request));
        verify(usuarioService, never()).register(any());
    }

    @Test
    void register_whenRepositoryFails_shouldRollbackAndThrow() {
        UsuarioSistemaRegister register = new UsuarioSistemaRegister("Test", "test@test.com", null, "group-id");
        Usuario usuario = mock(Usuario.class);
        when(usuario.id()).thenReturn("user-id");

        when(repository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(usuarioService.register(any(UsuarioRegister.class))).thenReturn(usuario);
        when(repository.register(any(UsuarioSistemaRegister.class))).thenThrow(new RuntimeException("DB error"));

        assertThrows(RuntimeException.class, () -> service.register(register));

        verify(usuarioService).delete("user-id");
        verify(events, never()).publishEvent(any());
    }

    @Test
    void register_success() throws Exception {
        UsuarioSistemaRegister register = new UsuarioSistemaRegister("Test", "test@test.com", null, "group-id");
        Usuario usuario = mock(Usuario.class);
        when(usuario.id()).thenReturn("user-id");
        UsuarioSistema registeredSystemUser = mock(UsuarioSistema.class);

        when(repository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(usuarioService.register(any(UsuarioRegister.class))).thenReturn(usuario);
        when(repository.register(any(UsuarioSistemaRegister.class))).thenReturn(registeredSystemUser);

        UsuarioSistema result = service.register(register);

        assertEquals(registeredSystemUser, result);
        ArgumentCaptor<UsuarioSistemaCriadoEvent> eventCaptor = ArgumentCaptor.forClass(UsuarioSistemaCriadoEvent.class);
        verify(events).publishEvent(eventCaptor.capture());
        assertEquals(registeredSystemUser, eventCaptor.getValue().usuarioSistema());
    }

    @Test
    void update_whenNotFound_shouldThrowNotFoundException() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.update(id, mock(UsuarioSistemaUpdate.class)));
        verify(usuarioService, never()).update(any(), any());
    }

    @Test
    void update_success() throws Exception {
        UUID id = UUID.randomUUID();
        var update = new UsuarioSistemaUpdate(true, "New Name", "new-group");
        var systemUser = mock(UsuarioSistema.class);
        when(systemUser.identityId()).thenReturn("identity-id");
        when(systemUser.email()).thenReturn("test@test.com");

        when(repository.findById(id)).thenReturn(Optional.of(systemUser));

        service.update(id, update);

        verify(usuarioService).update(eq("identity-id"), any(UsuarioUpdate.class));
        verify(repository).update(id, update);
        ArgumentCaptor<UsuarioSistemaAtualizadoEvent> eventCaptor = ArgumentCaptor.forClass(UsuarioSistemaAtualizadoEvent.class);
        verify(events).publishEvent(eventCaptor.capture());
        assertEquals(id, eventCaptor.getValue().id());
    }

    @Test
    void ativar_success() {
        UUID id = UUID.randomUUID();
        var systemUser = mock(UsuarioSistema.class);
        when(systemUser.identityId()).thenReturn("identity-id");
        when(repository.findById(id)).thenReturn(Optional.of(systemUser));

        service.ativar(id);

        verify(usuarioService).ativar("identity-id");
        verify(repository).ativar(id);
        ArgumentCaptor<UsuarioSistemaAtivadoEvent> eventCaptor = ArgumentCaptor.forClass(UsuarioSistemaAtivadoEvent.class);
        verify(events).publishEvent(eventCaptor.capture());
        assertEquals(systemUser, eventCaptor.getValue().usuarioSistema());
    }

    @Test
    void inativar_success() {
        UUID id = UUID.randomUUID();
        var systemUser = mock(UsuarioSistema.class);
        when(systemUser.identityId()).thenReturn("identity-id");
        when(repository.findById(id)).thenReturn(Optional.of(systemUser));

        service.inativar(id);

        verify(usuarioService).inativar("identity-id");
        verify(repository).inativar(id);
        ArgumentCaptor<UsuarioSistemaInativadoEvent> eventCaptor = ArgumentCaptor.forClass(UsuarioSistemaInativadoEvent.class);
        verify(events).publishEvent(eventCaptor.capture());
        assertEquals(systemUser, eventCaptor.getValue().usuarioSistema());
    }

    @Test
    void resetPassword_success() {
        UUID id = UUID.randomUUID();
        OffsetDateTime offsetDateTime = OffsetDateTime.of(2025, 11, 5, 20, 0, 0, 0, OffsetDateTime.now().getOffset());
        UsuarioSistema systemUser = new UsuarioSistema(id, "user-id", false, "email", "Name", Status.ATIVO, 1, offsetDateTime, offsetDateTime);
        when(repository.findById(id)).thenReturn(Optional.of(systemUser));

        service.resetPassword(id);

        verify(usuarioService).resetPassword(eq("user-id"), any());
    }
}
