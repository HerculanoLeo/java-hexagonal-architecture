package com.lodh8.starter.identity.grupos.app.impl;

import com.lodh8.starter.identity.grupos.app.port.GrupoProviderPort;
import com.lodh8.starter.identity.grupos.domain.Grupo;
import com.lodh8.starter.identity.grupos.domain.GrupoRegister;
import com.lodh8.starter.identity.grupos.domain.GrupoSearch;
import com.lodh8.starter.identity.grupos.domain.GrupoUpdate;
import com.lodh8.starter.identity.grupos.domain.events.GrupoCriadoEvent;
import com.lodh8.starter.identity.grupos.domain.events.GrupoDeletadoEvent;
import com.lodh8.starter.shared.events.app.EventPublisherPort;
import com.lodh8.starter.shared.exceptions.ConflictException;
import com.lodh8.starter.shared.models.enums.TipoAcesso;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GrupoServiceImplTest {

    @Mock
    private GrupoProviderPort grupoProvider;

    @Mock
    private EventPublisherPort events;

    @InjectMocks
    private GrupoServiceImpl service;

    @Test
    void findAll_shouldDelegateToProvider() {
        var search = mock(GrupoSearch.class);
        var expected = List.of(mock(Grupo.class));
        when(grupoProvider.findAll(search)).thenReturn(expected);

        Collection<Grupo> result = service.findAll(search);

        assertEquals(expected, result);
        verify(grupoProvider).findAll(search);
    }

    @Test
    void findById_shouldDelegateToProvider() {
        String id = "test-id";
        var expected = Optional.of(mock(Grupo.class));
        when(grupoProvider.findById(id)).thenReturn(expected);

        Optional<Grupo> result = service.findById(id);

        assertEquals(expected, result);
        verify(grupoProvider).findById(id);
    }

    @Test
    void findByIdentityId_shouldDelegateToProvider() {
        String identityId = "identity-123";
        var expected = Optional.of(mock(Grupo.class));
        when(grupoProvider.findByIdentityId(identityId)).thenReturn(expected);

        Optional<Grupo> result = service.findByIdentityId(identityId);

        assertEquals(expected, result);
        verify(grupoProvider).findByIdentityId(identityId);
    }

    @Test
    void register_whenNameExists_shouldThrowConflictException() {
        var register = mock(GrupoRegister.class);
        when(register.nome()).thenReturn("Existing Name");
        when(grupoProvider.existsByName("Existing Name")).thenReturn(true);

        assertThrows(ConflictException.class, () -> service.register(register));
        verify(grupoProvider, never()).register(any());
    }

    @Test
    void register_success_shouldRegisterUpdateRolesAndPublishEvent() {
        var roles = List.of("ROLE_1");
        var register = new GrupoRegister(null, TipoAcesso.USUARIO_SISTEMA, "New Name", roles);
        var grupo = mock(Grupo.class);

        when(grupoProvider.existsByName("New Name")).thenReturn(false);
        when(grupoProvider.register(register)).thenReturn(grupo);
        when(grupo.id()).thenReturn("new-id");
        when(grupo.tipo()).thenReturn(TipoAcesso.USUARIO_SISTEMA);

        Grupo result = service.register(register);

        assertEquals(grupo, result);
        verify(grupoProvider).updateRoles("new-id", TipoAcesso.USUARIO_SISTEMA, roles);
        ArgumentCaptor<GrupoCriadoEvent> eventCaptor = ArgumentCaptor.forClass(GrupoCriadoEvent.class);
        verify(events).publishEvent(eventCaptor.capture());
        assertEquals(grupo, eventCaptor.getValue().grupo());
    }

    @Test
    void update_whenGroupNotFound_shouldThrowNotFoundException() {
        String id = "not-found";
        when(grupoProvider.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.update(id, mock(GrupoUpdate.class)));
        verify(grupoProvider, never()).update(any(), any());
    }

    @Test
    void update_whenNewNameConflicts_shouldThrowConflictException() {
        String id = "group-id";
        var update = new GrupoUpdate("Existing Name", List.of());
        var grupo = mock(Grupo.class);
        when(grupo.nomeInterno()).thenReturn("Old Name");
        when(grupoProvider.findById(id)).thenReturn(Optional.of(grupo));
        when(grupoProvider.existsByName("Existing Name")).thenReturn(true);

        assertThrows(ConflictException.class, () -> service.update(id, update));
        verify(grupoProvider, never()).update(any(), any());
    }

    @Test
    void update_success_shouldUpdateAndSetRoles() {
        String id = "group-id";
        var roles = List.of("ROLE_2");
        var update = new GrupoUpdate("New Name", roles);
        var grupo = mock(Grupo.class);

        when(grupoProvider.findById(id)).thenReturn(Optional.of(grupo));
        when(grupo.nomeInterno()).thenReturn("New Name"); // Simulate name not changing
        when(grupo.tipo()).thenReturn(TipoAcesso.USUARIO_SISTEMA);

        service.update(id, update);

        verify(grupoProvider).update(eq(id), any(GrupoUpdate.class));
        verify(grupoProvider).updateRoles(id, TipoAcesso.USUARIO_SISTEMA, roles);
    }

    @Test
    void delete_shouldDeleteAndPublishEvent() {
        String id = "test-id";
        doNothing().when(grupoProvider).delete(id);

        service.delete(id);

        verify(grupoProvider).delete(id);
        ArgumentCaptor<GrupoDeletadoEvent> eventCaptor = ArgumentCaptor.forClass(GrupoDeletadoEvent.class);
        verify(events).publishEvent(eventCaptor.capture());
        assertEquals(id, eventCaptor.getValue().id());
    }

    @Test
    void hasMembers_shouldDelegateToProvider() {
        String id = "test-id";
        when(grupoProvider.hasMembers(id)).thenReturn(true);

        assertTrue(service.hasMembers(id));
        verify(grupoProvider).hasMembers(id);
    }
}
