package com.lodh8.starter.notification.api.controller;

import com.lodh8.starter.notification.api.dto.NotificacaoDTO;
import com.lodh8.starter.notification.api.dto.NotificacaoSearchDTO;
import com.lodh8.starter.notification.app.NotificacaoService;
import com.lodh8.starter.notification.app.NotificacaoTesteService;
import com.lodh8.starter.notification.domain.Notificacao;
import com.lodh8.starter.notification.domain.NotificacaoSearch;
import com.lodh8.starter.notification.infra.NotificacaoDTOMapper;
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
    private NotificacaoTesteService testeService;

    @Mock
    private NotificacaoDTOMapper mapper;

    @InjectMocks
    private NotificacaoController controller;

    @Test
    void findAll_shouldReturnOkWithListOfNotificacoes() {
        var searchDto = new NotificacaoSearchDTO();
        var domainSearch = mock(NotificacaoSearch.class);
        var domainNotificacao = mock(Notificacao.class);
        var dto = mock(NotificacaoDTO.class);

        when(mapper.domain(searchDto)).thenReturn(domainSearch);
        when(service.findAll(domainSearch)).thenReturn(List.of(domainNotificacao));
        when(mapper.dto(domainNotificacao)).thenReturn(dto);

        ResponseEntity<Collection<NotificacaoDTO>> response = controller.findAll(searchDto);

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
        UUID id = UUID.randomUUID();
        var domainNotificacao = mock(Notificacao.class);
        var dto = mock(NotificacaoDTO.class);

        when(service.findById(id)).thenReturn(Optional.of(domainNotificacao));
        when(mapper.dto(domainNotificacao)).thenReturn(dto);

        ResponseEntity<NotificacaoDTO> response = controller.findById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(dto, response.getBody());

        verify(service).findById(id);
        verify(mapper).dto(domainNotificacao);
    }

    @Test
    void findById_whenNotFound_shouldReturnOkWithEmptyOptional() {
        UUID id = UUID.randomUUID();
        when(service.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<NotificacaoDTO> response = controller.findById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        verify(service).findById(id);
        verify(mapper, never()).dto(any());
    }

    @Test
    void testeEmail_shouldDelegateToTesteService() {
        ResponseEntity<Void> response = controller.testeEmail();

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        verify(testeService).enviarEmailTeste();
    }
}
