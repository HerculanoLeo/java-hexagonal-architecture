package com.herculanoleo.starter.plataformadmin.grupos.api.controller;

import com.herculanoleo.starter.plataformadmin.grupos.api.dtos.GrupoSistemaDTO;
import com.herculanoleo.starter.plataformadmin.grupos.api.dtos.GrupoSistemaRegisterRequest;
import com.herculanoleo.starter.plataformadmin.grupos.api.dtos.GrupoSistemaSearchRequest;
import com.herculanoleo.starter.plataformadmin.grupos.api.dtos.GrupoSistemaUpdateRequest;
import com.herculanoleo.starter.plataformadmin.grupos.app.GrupoSistemaService;
import com.herculanoleo.starter.plataformadmin.grupos.domain.GrupoSistema;
import com.herculanoleo.starter.plataformadmin.grupos.domain.GrupoSistemaRegister;
import com.herculanoleo.starter.plataformadmin.grupos.domain.GrupoSistemaSearch;
import com.herculanoleo.starter.plataformadmin.grupos.domain.GrupoSistemaUpdate;
import com.herculanoleo.starter.plataformadmin.grupos.infra.GrupoSistemaDTOMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GrupoAutenticadoSistemaControllerTest {

    @Mock
    private GrupoSistemaService service;

    @Mock
    private GrupoSistemaDTOMapper mapper;

    @InjectMocks
    private GrupoSistemaController controller;

    @Test
    void findAll_shouldReturnOkWithListOfGrupos() {
        // Arrange
        var request = mock(GrupoSistemaSearchRequest.class);
        var domainSearch = mock(GrupoSistemaSearch.class);
        var domainGrupo = mock(GrupoSistema.class);
        var dto = mock(GrupoSistemaDTO.class);

        when(mapper.search(request)).thenReturn(domainSearch);
        when(service.findAll(domainSearch)).thenReturn(List.of(domainGrupo));
        when(mapper.dto(domainGrupo)).thenReturn(dto);

        // Act
        ResponseEntity<Collection<GrupoSistemaDTO>> response = controller.findAll(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertTrue(response.getBody().contains(dto));
        verify(service).findAll(domainSearch);
        verify(mapper).dto(domainGrupo);
    }

    @Test
    void findById_whenFound_shouldReturnOkWithGrupo() {
        // Arrange
        String id = "test-id";
        var domainGrupo = mock(GrupoSistema.class);
        var dto = mock(GrupoSistemaDTO.class);

        when(service.findById(id)).thenReturn(Optional.of(domainGrupo));
        when(mapper.dto(domainGrupo)).thenReturn(dto);

        // Act
        ResponseEntity<GrupoSistemaDTO> response = controller.findById(id);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
        verify(service).findById(id);
        verify(mapper).dto(domainGrupo);
    }

    @Test
    void findById_whenNotFound_shouldReturnOkWithEmptyOptional() {
        // Arrange
        String id = "not-found-id";
        when(service.findById(id)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<GrupoSistemaDTO> response = controller.findById(id);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(service).findById(id);
        verify(mapper, never()).dto(any());
    }

    @Test
    void register_shouldReturnCreatedWithGrupo() {
        // Arrange
        var request = mock(GrupoSistemaRegisterRequest.class);
        var domainRegister = mock(GrupoSistemaRegister.class);
        var registeredGrupo = mock(GrupoSistema.class);
        var dto = mock(GrupoSistemaDTO.class);

        when(mapper.register(request)).thenReturn(domainRegister);
        when(service.register(domainRegister)).thenReturn(registeredGrupo);
        when(mapper.dto(registeredGrupo)).thenReturn(dto);

        // Act
        ResponseEntity<GrupoSistemaDTO> response = controller.register(request);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(dto, response.getBody());
        verify(service).register(domainRegister);
        verify(mapper).dto(registeredGrupo);
    }

    @Test
    void update_shouldReturnNoContent() {
        // Arrange
        String id = "test-id";
        var request = mock(GrupoSistemaUpdateRequest.class);
        var domainUpdate = mock(GrupoSistemaUpdate.class);

        when(mapper.update(request)).thenReturn(domainUpdate);
        doNothing().when(service).update(id, domainUpdate);

        // Act
        ResponseEntity<Void> response = controller.update(id, request);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(service).update(id, domainUpdate);
        verify(mapper).update(request);
    }


    @Test
    void delete_shouldReturnNoContent() {
        // Arrange
        String id = "test-id";
        doNothing().when(service).delete(id);

        // Act
        ResponseEntity<Void> response = controller.delete(id);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(service).delete(id);
    }
}
