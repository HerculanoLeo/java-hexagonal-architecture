package com.lodh8.starter.authorize.api.controller;

import com.lodh8.starter.authorize.api.dto.GrupoAutenticadoDTO;
import com.lodh8.starter.authorize.api.dto.TrocaSenhaRequest;
import com.lodh8.starter.authorize.api.dto.UsuarioAutenticadoDTO;
import com.lodh8.starter.authorize.api.dto.UsuarioAutenticadoUpdateRequest;
import com.lodh8.starter.authorize.app.UsuarioAutenticadoService;
import com.lodh8.starter.authorize.domain.GrupoAutenticado;
import com.lodh8.starter.authorize.domain.UsuarioAutenticado;
import com.lodh8.starter.authorize.infra.UsuarioAutenticadoDTOMapper;
import com.lodh8.starter.identity.usuario.domain.TrocaSenha;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
        var domainUser = mock(UsuarioAutenticado.class);
        var userDto = mock(UsuarioAutenticadoDTO.class);

        when(service.usuarioAutenticado()).thenReturn(domainUser);
        when(mapper.dto(domainUser)).thenReturn(userDto);

        ResponseEntity<UsuarioAutenticadoDTO> response = controller.me();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDto, response.getBody());

        verify(service, times(1)).usuarioAutenticado();
        verify(mapper, times(1)).dto(domainUser);
    }

    @Test
    void updateMe_shouldReturnUpdatedUserDto() {
        var domainUser = mock(UsuarioAutenticado.class);
        var userDto = mock(UsuarioAutenticadoDTO.class);
        var request = new UsuarioAutenticadoUpdateRequest("New Name");

        when(service.updateMe("New Name")).thenReturn(domainUser);
        when(mapper.dto(domainUser)).thenReturn(userDto);

        ResponseEntity<UsuarioAutenticadoDTO> response = controller.updateMe(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDto, response.getBody());
        verify(service).updateMe("New Name");
    }

    @Test
    void changePassword_shouldCallServiceAndReturnNoContent() {
        var request = new TrocaSenhaRequest("old", "new", "new");

        ResponseEntity<Void> response = controller.changePassword(request);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        ArgumentCaptor<TrocaSenha> captor = ArgumentCaptor.forClass(TrocaSenha.class);
        verify(service).changePassword(captor.capture());
        assertEquals("old", captor.getValue().senhaAtual());
        assertEquals("new", captor.getValue().novaSenha());
        assertEquals("new", captor.getValue().confirmacaoSenha());
    }

    @Test
    void grupo_shouldReturnAuthenticatedGroupDto() {
        var domainGroup = mock(GrupoAutenticado.class);
        var groupDto = mock(GrupoAutenticadoDTO.class);

        when(service.grupoAutenticado()).thenReturn(domainGroup);
        when(mapper.dto(domainGroup)).thenReturn(groupDto);

        ResponseEntity<GrupoAutenticadoDTO> response = controller.grupo();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(groupDto, response.getBody());

        verify(service, times(1)).grupoAutenticado();
        verify(mapper, times(1)).dto(domainGroup);
    }

    @Test
    void grupo_whenNull_shouldReturnNoContent() {
        when(service.grupoAutenticado()).thenReturn(null);

        ResponseEntity<GrupoAutenticadoDTO> response = controller.grupo();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(mapper, never()).dto(any(GrupoAutenticado.class));
    }
}
