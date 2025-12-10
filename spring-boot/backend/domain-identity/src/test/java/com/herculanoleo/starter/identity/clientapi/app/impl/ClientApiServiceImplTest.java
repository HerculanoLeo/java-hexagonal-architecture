package com.herculanoleo.starter.identity.clientapi.app.impl;

import com.herculanoleo.starter.identity.clientapi.app.port.ClientApiProviderPort;
import com.herculanoleo.starter.identity.clientapi.domain.ClientApi;
import com.herculanoleo.starter.identity.clientapi.domain.ClientApiRegister;
import com.herculanoleo.starter.identity.clientapi.domain.ClientApiUpdate;
import com.herculanoleo.starter.identity.clientapi.domain.events.ClientApiAtivadoEvent;
import com.herculanoleo.starter.identity.clientapi.domain.events.ClientApiCriadoEvent;
import com.herculanoleo.starter.identity.clientapi.domain.events.ClientApiInativadoEvent;
import com.herculanoleo.starter.identity.clientapi.domain.events.ClientApiSecretRecriadaEvent;
import com.herculanoleo.starter.shared.events.app.EventPublisherPort;
import com.herculanoleo.starter.shared.exceptions.ConflictException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientApiServiceImplTest {

    private ClientApiProviderPort clientApiProvider;
    private EventPublisherPort events;
    private ClientApiServiceImpl service;

    @BeforeEach
    void setUp() {
        clientApiProvider = mock(ClientApiProviderPort.class);
        events = mock(EventPublisherPort.class);
        service = new ClientApiServiceImpl(clientApiProvider, events);
    }

    @Test
    void findById_shouldReturnResultFromProvider() {
        String id = "test-id";
        Optional<ClientApi> expected = Optional.of(mock(ClientApi.class));
        when(clientApiProvider.findById(id)).thenReturn(expected);

        Optional<ClientApi> result = service.findById(id);

        assertEquals(expected, result);
        verify(clientApiProvider).findById(id);
    }

    @Test
    void register_whenClientIdExists_shouldThrowConflictException() {
        ClientApiRegister register = mock(ClientApiRegister.class);
        when(register.clientId()).thenReturn("existing-id");
        when(clientApiProvider.existsByClientId("existing-id")).thenReturn(true);

        assertThrows(ConflictException.class, () -> service.register(register));

        verify(clientApiProvider, never()).register(any());
        verify(events, never()).publishEvent(any());
    }

    @Test
    void register_whenSuccess_shouldRegisterAndpublish() {
        ClientApiRegister register = mock(ClientApiRegister.class);
        when(register.clientId()).thenReturn("new-id");
        ClientApi clientApi = mock(ClientApi.class);

        when(clientApiProvider.existsByClientId("new-id")).thenReturn(false);
        when(clientApiProvider.register(register)).thenReturn(clientApi);

        ClientApi result = service.register(register);

        assertEquals(clientApi, result);
        verify(clientApiProvider).register(register);

        ArgumentCaptor<ClientApiCriadoEvent> eventCaptor = ArgumentCaptor.forClass(ClientApiCriadoEvent.class);
        verify(events).publishEvent(eventCaptor.capture());
        assertEquals(clientApi, eventCaptor.getValue().clientApi());
    }

    @Test
    void update_shouldCallProviderUpdate() {
        String id = "test-id";
        ClientApiUpdate update = mock(ClientApiUpdate.class);

        service.update(id, update);

        verify(clientApiProvider).update(id, update);
    }

    @Test
    void updateRelacionadoId_shouldCallProviderUpdateRelacionadoId() {
        String id = "test-id";
        String relacionadoId = "rel-id";

        service.updateRelacionadoId(id, relacionadoId);

        verify(clientApiProvider).updateRelacionadoId(id, relacionadoId);
    }

    @Test
    void ativar_shouldCallProviderAndPublishEvent() {
        String id = "test-id";

        service.ativar(id);

        verify(clientApiProvider).ativar(id);
        ArgumentCaptor<ClientApiAtivadoEvent> eventCaptor = ArgumentCaptor.forClass(ClientApiAtivadoEvent.class);
        verify(events).publishEvent(eventCaptor.capture());
        assertEquals(id, eventCaptor.getValue().id());
    }

    @Test
    void inativar_shouldCallProviderAndPublishEvent() {
        String id = "test-id";

        service.inativar(id);

        verify(clientApiProvider).inativar(id);
        ArgumentCaptor<ClientApiInativadoEvent> eventCaptor = ArgumentCaptor.forClass(ClientApiInativadoEvent.class);
        verify(events).publishEvent(eventCaptor.capture());
        assertEquals(id, eventCaptor.getValue().id());
    }

    @Test
    void delete_shouldCallProviderDelete() {
        String id = "test-id";

        service.delete(id);

        verify(clientApiProvider).delete(id);
        verify(events, never()).publishEvent(any());
    }

    @Test
    void regenerateSecret_shouldCallProviderAndPublishEvent() {
        String id = "test-id";

        service.regenerateSecret(id);

        verify(clientApiProvider).regenerateSecret(id);
        ArgumentCaptor<ClientApiSecretRecriadaEvent> eventCaptor = ArgumentCaptor.forClass(ClientApiSecretRecriadaEvent.class);
        verify(events).publishEvent(eventCaptor.capture());
        assertEquals(id, eventCaptor.getValue().id());
    }
}
