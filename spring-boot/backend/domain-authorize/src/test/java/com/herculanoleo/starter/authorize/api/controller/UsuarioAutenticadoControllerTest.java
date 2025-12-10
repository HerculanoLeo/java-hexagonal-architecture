package com.herculanoleo.starter.authorize.api.controller;

import com.herculanoleo.starter.authorize.api.dto.GrupoAutenticadoDTO;
import com.herculanoleo.starter.authorize.api.dto.UsuarioAutenticadoDTO;
import com.herculanoleo.starter.authorize.app.UsuarioAutenticadoService;
import com.herculanoleo.starter.authorize.domain.GrupoAutenticado;
import com.herculanoleo.starter.authorize.domain.UsuarioAutenticado;
import com.herculanoleo.starter.authorize.infra.UsuarioAutenticadoDTOMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioAutenticadoControllerTest {

    @Mock
    private UsuarioAutenticadoService service;

    @Mock
    private UsuarioAutenticadoDTOMapper mapper;

    @InjectMocks
    private UsuarioAutenticadoController controller;

    @Test
    void me_shouldReturnAuthenticatedUserDto() {
        // Arrange
        var domainUser = mock(UsuarioAutenticado.class);
        var userDto = mock(UsuarioAutenticadoDTO.class);

        when(service.usuarioAutenticado()).thenReturn(domainUser);
        when(mapper.dto(domainUser)).thenReturn(userDto);

        // Act
        ResponseEntity<UsuarioAutenticadoDTO> response = controller.me();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDto, response.getBody());

        verify(service, times(1)).usuarioAutenticado();
        verify(mapper, times(1)).dto(domainUser);
    }

    @Test
    void grupo_shouldReturnAuthenticatedGroupDto() {
        // Arrange
        var domainGroup = mock(GrupoAutenticado.class);
        var groupDto = mock(GrupoAutenticadoDTO.class);

        when(service.grupoAutenticado()).thenReturn(domainGroup);
        when(mapper.dto(domainGroup)).thenReturn(groupDto);

        // Act
        ResponseEntity<GrupoAutenticadoDTO> response = controller.grupo();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(groupDto, response.getBody());

        verify(service, times(1)).grupoAutenticado();
        verify(mapper, times(1)).dto(domainGroup);
    }
}
