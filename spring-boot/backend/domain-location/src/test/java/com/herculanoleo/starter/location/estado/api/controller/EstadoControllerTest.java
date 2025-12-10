package com.herculanoleo.starter.location.estado.api.controller;

import com.herculanoleo.starter.location.estado.api.dto.EstadoDTO;
import com.herculanoleo.starter.location.estado.api.dto.EstadoSearchDTO;
import com.herculanoleo.starter.location.estado.app.EstadoService;
import com.herculanoleo.starter.location.estado.domain.Estado;
import com.herculanoleo.starter.location.estado.domain.EstadoSearch;
import com.herculanoleo.starter.location.estado.infra.EstadoDTOMapper;
import com.herculanoleo.starter.shared.models.enums.EstadoSigla;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EstadoControllerTest {

    @Mock
    private EstadoService service;

    @Mock
    private EstadoDTOMapper mapper;

    @InjectMocks
    private EstadoController controller;

    @Test
    void findAll_shouldReturnListOfEstados() {
        // Arrange
        var searchDto = new EstadoSearchDTO();
        var domainSearch = mock(EstadoSearch.class);
        var domainEstado = mock(Estado.class);
        var dto = mock(EstadoDTO.class);

        when(mapper.domain(searchDto)).thenReturn(domainSearch);
        when(service.findAll(domainSearch)).thenReturn(Collections.singletonList(domainEstado));
        when(mapper.dto(domainEstado)).thenReturn(dto);

        // Act
        ResponseEntity<Collection<EstadoDTO>> response = controller.findAll(searchDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertTrue(response.getBody().contains(dto));
    }

    @Test
    void findById_whenFound_shouldReturnOkWithDto() {
        // Arrange
        Long id = 1L;
        var domainEstado = mock(Estado.class);
        var dto = mock(EstadoDTO.class);

        when(service.findById(id)).thenReturn(Optional.of(domainEstado));
        when(mapper.dto(domainEstado)).thenReturn(dto);

        // Act
        ResponseEntity<EstadoDTO> response = controller.findById(id);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
    }

    @Test
    void findById_whenNotFound_shouldReturnNotFound() {
        // Arrange
        Long id = 1L;
        when(service.findById(id)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<EstadoDTO> response = controller.findById(id);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(mapper, never()).dto(any());
    }

    @Test
    void findBySigla_whenFound_shouldReturnOkWithDto() {
        // Arrange
        var sigla = EstadoSigla.SAO_PAULO;
        var domainEstado = mock(Estado.class);
        var dto = mock(EstadoDTO.class);

        when(service.findBySigla(sigla)).thenReturn(Optional.of(domainEstado));
        when(mapper.dto(domainEstado)).thenReturn(dto);

        // Act
        ResponseEntity<EstadoDTO> response = controller.findBySigla(sigla);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
    }

    @Test
    void findBySigla_whenNotFound_shouldReturnNotFound() {
        // Arrange
        var sigla = EstadoSigla.SAO_PAULO;
        when(service.findBySigla(sigla)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<EstadoDTO> response = controller.findBySigla(sigla);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(mapper, never()).dto(any());
    }
}
