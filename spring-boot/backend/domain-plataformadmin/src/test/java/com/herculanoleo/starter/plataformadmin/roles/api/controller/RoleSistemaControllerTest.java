package com.herculanoleo.starter.plataformadmin.roles.api.controller;

import com.herculanoleo.starter.plataformadmin.roles.api.dtos.RoleSistemaDTO;
import com.herculanoleo.starter.plataformadmin.roles.app.RoleSistemaService;
import com.herculanoleo.starter.plataformadmin.roles.domain.RoleSistema;
import com.herculanoleo.starter.plataformadmin.roles.infra.RoleSistemaDTOMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleSistemaControllerTest {

    @Mock
    private RoleSistemaService service;

    @Mock
    private RoleSistemaDTOMapper mapper;

    @InjectMocks
    private RoleSistemaController controller;

    @Test
    void roles_shouldReturnOkWithListOfRoles() {
        // Arrange
        RoleSistema domainRole1 = mock(RoleSistema.class);
        RoleSistema domainRole2 = mock(RoleSistema.class);
        List<RoleSistema> domainRoles = List.of(domainRole1, domainRole2);

        RoleSistemaDTO dto1 = mock(RoleSistemaDTO.class);
        RoleSistemaDTO dto2 = mock(RoleSistemaDTO.class);

        when(service.roles()).thenReturn(domainRoles);
        when(mapper.dto(domainRole1)).thenReturn(dto1);
        when(mapper.dto(domainRole2)).thenReturn(dto2);

        // Act
        ResponseEntity<Collection<RoleSistemaDTO>> response = controller.roles();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertTrue(response.getBody().containsAll(List.of(dto1, dto2)));

        // Verify interactions
        verify(service, times(1)).roles();
        verify(mapper, times(1)).dto(domainRole1);
        verify(mapper, times(1)).dto(domainRole2);
        verifyNoMoreInteractions(service, mapper);
    }
}
