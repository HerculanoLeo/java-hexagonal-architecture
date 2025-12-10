package com.herculanoleo.starter.location.municipio.api.controller;

import com.herculanoleo.starter.location.municipio.api.dto.MunicipioDTO;
import com.herculanoleo.starter.location.municipio.api.dto.MunicipioRegisterDTO;
import com.herculanoleo.starter.location.municipio.api.dto.MunicipioSearchDTO;
import com.herculanoleo.starter.location.municipio.api.dto.MunicipioUpdateDTO;
import com.herculanoleo.starter.location.municipio.app.MunicipioService;
import com.herculanoleo.starter.location.municipio.domain.Municipio;
import com.herculanoleo.starter.location.municipio.domain.MunicipioRegister;
import com.herculanoleo.starter.location.municipio.domain.MunicipioSearch;
import com.herculanoleo.starter.location.municipio.domain.MunicipioUpdate;
import com.herculanoleo.starter.location.municipio.infra.MunicipioDTOMapper;
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
class MunicipioControllerTest {

    @Mock
    private MunicipioService service;

    @Mock
    private MunicipioDTOMapper mapper;

    @InjectMocks
    private MunicipioController controller;

    @Test
    void findAll_shouldReturnListOfMunicipios() {
        // Arrange
        var searchDto = mock(MunicipioSearchDTO.class);
        var domainSearch = mock(MunicipioSearch.class);
        var domainMunicipio = mock(Municipio.class);
        var dto = mock(MunicipioDTO.class);

        when(mapper.domain(searchDto)).thenReturn(domainSearch);
        when(service.findAll(domainSearch)).thenReturn(Collections.singletonList(domainMunicipio));
        when(mapper.dto(domainMunicipio)).thenReturn(dto);

        // Act
        ResponseEntity<Collection<MunicipioDTO>> response = controller.findAll(searchDto);

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
        var domainMunicipio = mock(Municipio.class);
        var dto = mock(MunicipioDTO.class);

        when(service.findById(id)).thenReturn(Optional.of(domainMunicipio));
        when(mapper.dto(domainMunicipio)).thenReturn(dto);

        // Act
        ResponseEntity<MunicipioDTO> response = controller.findById(id);

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
        ResponseEntity<MunicipioDTO> response = controller.findById(id);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void register_shouldReturnCreatedWithDto() {
        // Arrange
        var request = mock(MunicipioRegisterDTO.class);
        var domainRegister = mock(MunicipioRegister.class);
        var registeredMunicipio = mock(Municipio.class);
        var resultDto = mock(MunicipioDTO.class);

        when(mapper.domain(request)).thenReturn(domainRegister);
        when(service.register(domainRegister)).thenReturn(registeredMunicipio);
        when(mapper.dto(registeredMunicipio)).thenReturn(resultDto);

        // Act
        ResponseEntity<MunicipioDTO> response = controller.register(request);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(resultDto, response.getBody());
    }

    @Test
    void update_shouldReturnNoContent() {
        // Arrange
        Long id = 1L;
        var request = mock(MunicipioUpdateDTO.class);
        var domainUpdate = mock(MunicipioUpdate.class);

        when(mapper.domain(request)).thenReturn(domainUpdate);
        doNothing().when(service).update(id, domainUpdate);

        // Act
        ResponseEntity<Void> response = controller.update(id, request);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(service).update(id, domainUpdate);
    }

    @Test
    void delete_shouldReturnNoContent() {
        // Arrange
        Long id = 1L;
        doNothing().when(service).delete(id);

        // Act
        ResponseEntity<Void> response = controller.delete(id);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(service).delete(id);
    }
}
