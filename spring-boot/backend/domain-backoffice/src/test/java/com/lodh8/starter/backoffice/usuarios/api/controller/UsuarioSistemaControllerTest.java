package com.lodh8.starter.backoffice.usuarios.api.controller;

import com.lodh8.starter.backoffice.usuarios.api.dtos.*;
import com.lodh8.starter.backoffice.usuarios.app.UsuarioSistemaService;
import com.lodh8.starter.backoffice.usuarios.domain.*;
import com.lodh8.starter.backoffice.usuarios.infra.UsuarioSistemaDTOMapper;
import com.lodh8.starter.shared.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioSistemaControllerTest {

    @Mock
    private UsuarioSistemaService service;

    @Mock
    private UsuarioSistemaDTOMapper mapper;

    @InjectMocks
    private UsuarioSistemaController controller;

    @Test
    void findAll_shouldReturnListOfUsers() {
        var searchRequest = new UsuarioSistemaSearchRequest();
        var domainSearch = mock(UsuarioSistemaSearch.class);
        var domainUser = mock(UsuarioSistema.class);
        var userDto = mock(UsuarioSistemaDTO.class);

        when(mapper.domain(searchRequest)).thenReturn(domainSearch);
        when(service.findAll(domainSearch)).thenReturn(Collections.singletonList(domainUser));
        when(mapper.dto(domainUser)).thenReturn(userDto);

        ResponseEntity<Collection<UsuarioSistemaDTO>> response = controller.findAll(searchRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertTrue(response.getBody().contains(userDto));
    }

    @Test
    void findById_whenUserExists_shouldReturnUser() {
        UUID id = UUID.randomUUID();
        var domainUser = mock(UsuarioSistema.class);
        var userDto = mock(UsuarioSistemaDTO.class);

        when(service.findById(id)).thenReturn(Optional.of(domainUser));
        when(mapper.dto(domainUser)).thenReturn(userDto);

        ResponseEntity<UsuarioSistemaDTO> response = controller.findById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDto, response.getBody());
    }

    @Test
    void findById_whenUserDoesNotExist_shouldThrowNotFound() {
        UUID id = UUID.randomUUID();
        when(service.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> controller.findById(id));
    }

    @Test
    void findGrupoById_whenFound_shouldReturnGrupoDto() {
        UUID id = UUID.randomUUID();
        var domainGroup = mock(UsuarioSistemaGrupo.class);
        var groupDto = mock(UsuarioSistemaGrupoDTO.class);

        when(service.findById(id)).thenReturn(Optional.of(mock(UsuarioSistema.class)));
        when(service.findGrupoById(id)).thenReturn(Optional.of(domainGroup));
        when(mapper.dto(domainGroup)).thenReturn(groupDto);

        ResponseEntity<UsuarioSistemaGrupoDTO> response = controller.findGrupoById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(groupDto, response.getBody());
    }

    @Test
    void findGrupoById_whenUserHasNoGroup_shouldReturnNoContent() {
        UUID id = UUID.randomUUID();

        when(service.findById(id)).thenReturn(Optional.of(mock(UsuarioSistema.class)));
        when(service.findGrupoById(id)).thenReturn(Optional.empty());

        ResponseEntity<UsuarioSistemaGrupoDTO> response = controller.findGrupoById(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(mapper, never()).dto(any(UsuarioSistemaGrupo.class));
    }

    @Test
    void findGrupoById_whenUserNotFound_shouldThrowNotFound() {
        UUID id = UUID.randomUUID();

        when(service.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> controller.findGrupoById(id));
        verify(service, never()).findGrupoById(any());
    }

    @Test
    void register_shouldCreateUserAndReturnIt() throws Exception {
        var request = mock(UsuarioSistemaRegisterRequest.class);
        var domainRegister = mock(UsuarioSistemaRegister.class);
        var registeredUser = mock(UsuarioSistema.class);
        var userDto = mock(UsuarioSistemaDTO.class);

        when(mapper.domain(request)).thenReturn(domainRegister);
        when(service.register(domainRegister)).thenReturn(registeredUser);
        when(mapper.dto(registeredUser)).thenReturn(userDto);

        ResponseEntity<UsuarioSistemaDTO> response = controller.register(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDto, response.getBody());
    }

    @Test
    void update_shouldCallServiceAndReturnNoContent() throws Exception {
        UUID id = UUID.randomUUID();
        var request = mock(UsuarioSistemaUpdateRequest.class);
        var domainUpdate = mock(UsuarioSistemaUpdate.class);

        when(mapper.domain(request)).thenReturn(domainUpdate);
        doNothing().when(service).update(id, domainUpdate);

        ResponseEntity<Void> response = controller.update(id, request);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(service).update(id, domainUpdate);
    }

    @Test
    void ativar_shouldCallServiceAndReturnAccepted() {
        UUID id = UUID.randomUUID();
        doNothing().when(service).ativar(id);

        ResponseEntity<Void> response = controller.ativar(id);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        verify(service).ativar(id);
    }

    @Test
    void inativar_shouldCallServiceAndReturnAccepted() {
        UUID id = UUID.randomUUID();
        doNothing().when(service).inativar(id);

        ResponseEntity<Void> response = controller.inativar(id);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        verify(service).inativar(id);
    }

    @Test
    void resetPassword_shouldCallServiceAndReturnAccepted() {
        UUID id = UUID.randomUUID();
        doNothing().when(service).resetPassword(id);

        ResponseEntity<Void> response = controller.resetPassword(id);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        verify(service).resetPassword(id);
    }
}
