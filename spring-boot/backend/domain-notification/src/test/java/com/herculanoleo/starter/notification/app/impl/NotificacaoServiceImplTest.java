package com.herculanoleo.starter.notification.app.impl;

import com.herculanoleo.starter.notification.app.ports.NotificacaoRepositoryPort;
import com.herculanoleo.starter.notification.app.ports.NotificacaoSenderPort;
import com.herculanoleo.starter.notification.domain.Notificacao;
import com.herculanoleo.starter.notification.domain.NotificacaoError;
import com.herculanoleo.starter.notification.domain.NotificacaoRegister;
import com.herculanoleo.starter.notification.domain.NotificacaoSearch;
import com.herculanoleo.starter.notification.domain.events.NotificacaoRegisterEvent;
import com.herculanoleo.starter.shared.events.app.EventPublisherPort;
import com.herculanoleo.starter.shared.exceptions.BadRequestException;
import com.herculanoleo.starter.shared.models.enums.NotificacaoStatus;
import com.herculanoleo.starter.shared.models.enums.TipoNotificacao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificacaoServiceImplTest {

    @Mock
    private NotificacaoRepositoryPort repository;

    @Mock
    private List<NotificacaoSenderPort> senders;

    @Mock
    private EventPublisherPort events;

    @InjectMocks
    @Spy
    private NotificacaoServiceImpl service;

    @Test
    void findAll_shouldCallRepository() {
        // Arrange
        var search = mock(NotificacaoSearch.class);
        var expected = Collections.singletonList(mock(Notificacao.class));
        when(repository.findAll(search)).thenReturn(expected);

        // Act
        Collection<Notificacao> result = service.findAll(search);

        // Assert
        assertEquals(expected, result);
        verify(repository).findAll(search);
    }

    @Test
    void findById_shouldCallRepository() {
        // Arrange
        var id = UUID.randomUUID();
        var expected = Optional.of(mock(Notificacao.class));
        when(repository.findById(id)).thenReturn(expected);

        // Act
        Optional<Notificacao> result = service.findById(id);

        // Assert
        assertEquals(expected, result);
        verify(repository).findById(id);
    }

    @Test
    void register_shouldCallRepositoryAndPublishEvent() {
        // Arrange
        var register = mock(NotificacaoRegister.class);
        var notificacao = mock(Notificacao.class);
        when(repository.register(register)).thenReturn(notificacao);

        // Act
        Notificacao result = service.register(register);

        // Assert
        assertEquals(notificacao, result);
        verify(repository).register(register);
        ArgumentCaptor<NotificacaoRegisterEvent> eventCaptor = ArgumentCaptor.forClass(NotificacaoRegisterEvent.class);
        verify(events).publishEvent(eventCaptor.capture());
        assertEquals(notificacao, eventCaptor.getValue().notificacao());
    }

    @Test
    void enviarById_whenStatusIsNotPendente_shouldThrowBadRequest() {
        // Arrange
        UUID id = UUID.randomUUID();
        var notificacao = mock(Notificacao.class);
        when(notificacao.status()).thenReturn(NotificacaoStatus.ENVIADO);
        when(repository.findById(id)).thenReturn(Optional.of(notificacao));

        // Act & Assert
        assertThrows(BadRequestException.class, () -> service.enviar(id));
        verify(service, never()).enviar(any(Notificacao.class));
    }

    @Test
    void enviarById_whenStatusIsPendente_shouldCallProtectedEnviar() {
        // Arrange
        UUID id = UUID.randomUUID();
        var notificacao = mock(Notificacao.class);
        when(notificacao.status()).thenReturn(NotificacaoStatus.PENDENTE);
        when(repository.findById(id)).thenReturn(Optional.of(notificacao));
        doNothing().when(service).enviar(notificacao); // Spy on the protected method

        // Act
        service.enviar(id);

        // Assert
        verify(service).enviar(notificacao);
    }

    @Test
    void reenvio_whenNoNotificacoes_shouldDoNothing() {
        // Arrange
        doReturn(Collections.emptyList()).when(service).findAll(any());

        // Act
        service.reenvio(null, null, 5);

        // Assert
        verify(service, never()).enviar(any(Notificacao.class));
    }

    @Test
    void reenvio_whenNotificacoesExist_shouldAttemptToSendEach() {
        // Arrange
        var notificacao1 = mock(Notificacao.class);
        var notificacao2 = mock(Notificacao.class);
        List<Notificacao> notificacoes = List.of(notificacao1, notificacao2);

        // Since reenvio calls the public findAll, we can spy it
        doReturn(notificacoes).when(service).findAll(any(NotificacaoSearch.class));
        // We also need to stub the protected enviar method that is called inside the executor
        doNothing().when(service).enviar(any(Notificacao.class));

        // Act
        service.reenvio(null, null, 5);

        // Assert
        // Because the execution is in a virtual thread, it might not have completed
        // by the time the verify call is made. A short wait can help in a test environment.
        // For a more robust solution, one would use CountDownLatches or other concurrency tools.
        // However, for this unit test, verifying the call was made is the main goal.
        // A timeout on verify() is a good compromise.
        verify(service, timeout(1000).times(1)).enviar(notificacao1);
        verify(service, timeout(1000).times(1)).enviar(notificacao2);
    }

    @Test
    void reenvio_whenFindAllThrowsException_shouldCatchAndLog() {
        // Arrange
        doThrow(new RuntimeException("Database connection failed")).when(service).findAll(any(NotificacaoSearch.class));

        // Act & Assert
        // The test will pass if no exception is thrown, as it should be caught by the catch block.
        assertDoesNotThrow(() -> service.reenvio(null, null, 5));

        // Also, verify that no attempt was made to send notifications
        verify(service, never()).enviar(any(Notificacao.class));
    }


    @Test
    void protectedEnviar_whenSenderFound_shouldSendAndMarkAsEnviado() {
        // Arrange
        var notificacao = mock(Notificacao.class);
        when(notificacao.id()).thenReturn(UUID.randomUUID());
        when(notificacao.tipo()).thenReturn(TipoNotificacao.EMAIL);

        var sender = mock(NotificacaoSenderPort.class);
        when(sender.suporta(TipoNotificacao.EMAIL)).thenReturn(true);
        // Mock the behavior of the stream
        when(senders.stream()).thenReturn(List.of(sender).stream());

        // Act
        service.enviar(notificacao);

        // Assert
        verify(sender).enviar(notificacao);
        verify(repository).enviado(notificacao.id());
        verify(repository, never()).erro(any(), any(), any());
    }

    @Test
    void protectedEnviar_whenSenderThrowsException_shouldMarkAsErro() {
        // Arrange
        var notificacao = mock(Notificacao.class);
        when(notificacao.id()).thenReturn(UUID.randomUUID());
        when(notificacao.tipo()).thenReturn(TipoNotificacao.EMAIL);
        when(notificacao.tentativas()).thenReturn(1);

        var sender = mock(NotificacaoSenderPort.class);
        when(sender.suporta(TipoNotificacao.EMAIL)).thenReturn(true);
        doThrow(new RuntimeException("Mail server down")).when(sender).enviar(notificacao);
        when(senders.stream()).thenReturn(List.of(sender).stream());

        // Act
        service.enviar(notificacao);

        // Assert
        verify(sender).enviar(notificacao);
        verify(repository, never()).enviado(any());
        ArgumentCaptor<NotificacaoError> errorCaptor = ArgumentCaptor.forClass(NotificacaoError.class);
        verify(repository).erro(eq(notificacao.id()), eq(2), errorCaptor.capture());
        assertEquals("Mail server down", errorCaptor.getValue().message());
    }

    @Test
    void protectedEnviar_whenNoSenderFound_shouldMarkAsErro() {
        // Arrange
        var notificacao = mock(Notificacao.class);
        when(notificacao.id()).thenReturn(UUID.randomUUID());
        when(notificacao.tipo()).thenReturn(TipoNotificacao.SMS);
        when(notificacao.tentativas()).thenReturn(0);

        // No sender supports SMS
        var sender = mock(NotificacaoSenderPort.class);
        when(sender.suporta(TipoNotificacao.SMS)).thenReturn(false);
        when(senders.stream()).thenReturn(List.of(sender).stream());

        // Act
        service.enviar(notificacao);

        // Assert
        verify(sender, never()).enviar(any());
        verify(repository, never()).enviado(any());
        ArgumentCaptor<NotificacaoError> errorCaptor = ArgumentCaptor.forClass(NotificacaoError.class);
        verify(repository).erro(eq(notificacao.id()), eq(1), errorCaptor.capture());
        assertEquals("tipo de notificação não implementada", errorCaptor.getValue().message());
    }
}
