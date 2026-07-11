package com.lodh8.starter.backoffice.usuarios.infra;

import com.lodh8.starter.backoffice.usuarios.domain.UsuarioSistema;
import com.lodh8.starter.backoffice.usuarios.domain.event.UsuarioSistemaAtivadoEvent;
import com.lodh8.starter.backoffice.usuarios.domain.event.UsuarioSistemaCriadoEvent;
import com.lodh8.starter.backoffice.usuarios.domain.event.UsuarioSistemaInativadoEvent;
import com.lodh8.starter.notification.domain.events.NotificacaoBoasVindasEvent;
import com.lodh8.starter.notification.domain.events.NotificacaoUsuarioAtivadoEvent;
import com.lodh8.starter.notification.domain.events.NotificacaoUsuarioInativadoEvent;
import com.lodh8.starter.shared.events.app.EventPublisherPort;
import com.lodh8.starter.shared.models.enums.TipoAcesso;
import com.lodh8.starter.shared.models.enums.TipoNotificacao;
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
        var usuarioSistema = mock(UsuarioSistema.class);
        when(usuarioSistema.nome()).thenReturn("Test User");
        when(usuarioSistema.email()).thenReturn("test@example.com");

        listeners.onUsuarioSistemaCriadoEvent(new UsuarioSistemaCriadoEvent(usuarioSistema));

        ArgumentCaptor<NotificacaoBoasVindasEvent> captor = ArgumentCaptor.forClass(NotificacaoBoasVindasEvent.class);
        verify(events).publishEvent(captor.capture());

        NotificacaoBoasVindasEvent publishedEvent = captor.getValue();
        assertEquals("Test User", publishedEvent.nome());
        assertEquals("test@example.com", publishedEvent.destinatario());
        assertEquals(TipoAcesso.USUARIO_SISTEMA, publishedEvent.tipoAcesso());
        assertEquals(TipoNotificacao.EMAIL, publishedEvent.tipoNotificacao());
    }

    @Test
    void onUsuarioSistemaAtivadoEvent_shouldPublishNotificacaoUsuarioAtivadoEvent() {
        var usuarioSistema = mock(UsuarioSistema.class);
        when(usuarioSistema.nome()).thenReturn("Test User");
        when(usuarioSistema.email()).thenReturn("test@example.com");

        listeners.onUsuarioSistemaAtivadoEvent(new UsuarioSistemaAtivadoEvent(usuarioSistema));

        ArgumentCaptor<NotificacaoUsuarioAtivadoEvent> captor = ArgumentCaptor.forClass(NotificacaoUsuarioAtivadoEvent.class);
        verify(events).publishEvent(captor.capture());

        assertEquals("Test User", captor.getValue().nome());
        assertEquals("test@example.com", captor.getValue().destinatario());
        assertEquals(TipoNotificacao.EMAIL, captor.getValue().tipoNotificacao());
    }

    @Test
    void onUsuarioSistemaInativadoEvent_shouldPublishNotificacaoUsuarioInativadoEvent() {
        var usuarioSistema = mock(UsuarioSistema.class);
        when(usuarioSistema.nome()).thenReturn("Test User");
        when(usuarioSistema.email()).thenReturn("test@example.com");

        listeners.onUsuarioSistemaInativadoEvent(new UsuarioSistemaInativadoEvent(usuarioSistema));

        ArgumentCaptor<NotificacaoUsuarioInativadoEvent> captor = ArgumentCaptor.forClass(NotificacaoUsuarioInativadoEvent.class);
        verify(events).publishEvent(captor.capture());

        assertEquals("Test User", captor.getValue().nome());
        assertEquals("test@example.com", captor.getValue().destinatario());
        assertEquals(TipoNotificacao.EMAIL, captor.getValue().tipoNotificacao());
    }
}
