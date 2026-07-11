package com.lodh8.starter.location.cep.api.controller;

import com.lodh8.starter.location.cep.api.dto.CEPDTO;
import com.lodh8.starter.location.cep.app.CepService;
import com.lodh8.starter.location.cep.domain.CEP;
import com.lodh8.starter.location.cep.infra.CEPDTOMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CEPControllerTest {

    @Mock
    private CepService service;

    @Mock
    private CEPDTOMapper mapper;

    @InjectMocks
    private CEPController controller;

    @Test
    void findByCep_whenFound_shouldReturnOkWithDto() {
        // Arrange
        String cep = "12345-678";
        var domainCep = mock(CEP.class);
        var dto = mock(CEPDTO.class);

        when(service.findByCep(cep)).thenReturn(Optional.of(domainCep));
        when(mapper.dto(domainCep)).thenReturn(dto);

        // Act
        ResponseEntity<CEPDTO> response = controller.findByCep(cep);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
    }

    @Test
    void findByCep_whenNotFound_shouldReturnNotFound() {
        // Arrange
        String cep = "00000-000";
        when(service.findByCep(cep)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<CEPDTO> response = controller.findByCep(cep);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(mapper, never()).dto(any());
    }
}
