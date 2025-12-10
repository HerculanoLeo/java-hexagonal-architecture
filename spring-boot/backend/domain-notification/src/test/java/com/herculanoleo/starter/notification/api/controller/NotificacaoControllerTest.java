package com.herculanoleo.starter.notification.api.controller;

import com.herculanoleo.starter.notification.api.dto.NotificacaoDTO;
import com.herculanoleo.starter.notification.api.dto.NotificacaoSearchDTO;
import com.herculanoleo.starter.notification.app.NotificacaoService;
import com.herculanoleo.starter.notification.domain.Notificacao;
import com.herculanoleo.starter.notification.domain.NotificacaoSearch;
import com.herculanoleo.starter.notification.infra.NotificacaoDTOMapper;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificacaoControllerTest {

    @Mock
    private NotificacaoService service;

    @Mock
    private NotificacaoDTOMapper mapper;

    @InjectMocks
    private NotificacaoController controller;

    @Test
    void findAll_shouldReturnOkWithListOfNotificacoes() {
        // Arrange
        var searchDto = new NotificacaoSearchDTO();
        var domainSearch = mock(NotificacaoSearch.class);
        var domainNotificacao = mock(Notificacao.class);
        var dto = mock(NotificacaoDTO.class);

        when(mapper.domain(searchDto)).thenReturn(domainSearch);
        when(service.findAll(domainSearch)).thenReturn(List.of(domainNotificacao));
        when(mapper.dto(domainNotificacao)).thenReturn(dto);

        // Act
        ResponseEntity<Collection<NotificacaoDTO>> response = controller.findAll(searchDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertTrue(response.getBody().contains(dto));

        verify(mapper).domain(searchDto);
        verify(service).findAll(domainSearch);
        verify(mapper).dto(domainNotificacao);
    }

    @Test
    void findById_whenFound_shouldReturnOkWithDtoInOptional() {
        // Arrange
        UUID id = UUID.randomUUID();
        var domainNotificacao = mock(Notificacao.class);
        var dto = mock(NotificacaoDTO.class);

        when(service.findById(id)).thenReturn(Optional.of(domainNotificacao));
        when(mapper.dto(domainNotificacao)).thenReturn(dto);

        // Act
        ResponseEntity<NotificacaoDTO> response = controller.findById(id);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(dto, response.getBody());

        verify(service).findById(id);
        verify(mapper).dto(domainNotificacao);
    }

    @Test
    void findById_whenNotFound_shouldReturnOkWithEmptyOptional() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(service.findById(id)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<NotificacaoDTO> response = controller.findById(id);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        verify(service).findById(id);
        verify(mapper, never()).dto(any());
    }
}
