package com.herculanoleo.starter.plataformadmin.usuarios.infra;

import com.herculanoleo.starter.notification.domain.events.NotificacaoBoasVindasEvent;
import com.herculanoleo.starter.plataformadmin.usuarios.domain.UsuarioSistema;
import com.herculanoleo.starter.plataformadmin.usuarios.domain.event.UsuarioSistemaCriadoEvent;
import com.herculanoleo.starter.shared.events.app.EventPublisherPort;
import com.herculanoleo.starter.shared.models.enums.TipoAcesso;
import com.herculanoleo.starter.shared.models.enums.TipoNotificacao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioSistemaListenersTest {

    @Mock
    private EventPublisherPort events;

    @InjectMocks
    private UsuarioSistemaListeners listeners;

    @Test
    void onUsuarioSistemaCriadoEvent_shouldPublishNotificacaoBoasVindasEvent() {
        // Arrange
        var usuarioSistema = mock(UsuarioSistema.class);
        when(usuarioSistema.nome()).thenReturn("Test User");
        when(usuarioSistema.email()).thenReturn("test@example.com");

        var criadoEvent = new UsuarioSistemaCriadoEvent(usuarioSistema);

        // Act
        listeners.onUsuarioSistemaCriadoEvent(criadoEvent);

        // Assert
        ArgumentCaptor<NotificacaoBoasVindasEvent> captor = ArgumentCaptor.forClass(NotificacaoBoasVindasEvent.class);
        verify(events).publishEvent(captor.capture());

        NotificacaoBoasVindasEvent publishedEvent = captor.getValue();
        assertEquals("Test User", publishedEvent.nome());
        assertEquals("test@example.com", publishedEvent.destinatario());
        assertEquals(TipoAcesso.USUARIO_SISTEMA, publishedEvent.tipoAcesso());
        assertEquals(TipoNotificacao.EMAIL, publishedEvent.tipoNotificacao());
    }
}
