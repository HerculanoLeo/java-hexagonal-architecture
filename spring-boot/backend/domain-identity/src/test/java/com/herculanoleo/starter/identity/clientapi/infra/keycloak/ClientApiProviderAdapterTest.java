package com.herculanoleo.starter.identity.clientapi.infra.keycloak;

import com.herculanoleo.starter.identity.clientapi.domain.ClientApi;
import com.herculanoleo.starter.identity.clientapi.domain.ClientApiRegister;
import com.herculanoleo.starter.identity.clientapi.domain.ClientApiUpdate;
import com.herculanoleo.starter.identity.clientapi.infra.ClientApiMapper;
import com.herculanoleo.starter.identity.roles.app.port.RoleProviderPort;
import com.herculanoleo.starter.identity.roles.domain.Role;
import com.herculanoleo.starter.identity.roles.infra.RoleMapper;
import com.herculanoleo.starter.shared.models.enums.TipoAcesso;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientApiProviderAdapterTest {

    private ClientsResource clientsResource;
    private UsersResource usersResource;
    private ClientApiMapper clientApiMapper;
    private RoleProviderPort roleProvider;
    private RoleMapper roleMapper;
    private ClientApiProviderAdapter adapter;

    @BeforeEach
    void setUp() {
        clientsResource = mock(ClientsResource.class);
        usersResource = mock(UsersResource.class);
        clientApiMapper = mock(ClientApiMapper.class);
        roleProvider = mock(RoleProviderPort.class);
        roleMapper = mock(RoleMapper.class);
        adapter = new ClientApiProviderAdapter(clientsResource, usersResource, clientApiMapper, roleProvider, roleMapper);
    }

    @Test
    void findById_whenClientExists_shouldReturnClientApi() {
        String clientId = "test-client-id";
        String userId = "test-user-id";
        ClientResource clientResource = mock(ClientResource.class);
        UserResource userResource = mock(UserResource.class);
        ClientRepresentation clientRep = new ClientRepresentation();
        clientRep.setId(clientId);
        clientRep.setClientId("client-id");
        clientRep.setName("client-name");
        clientRep.setEnabled(true);

        UserRepresentation userRep = new UserRepresentation();
        userRep.setId(userId);
        userRep.setAttributes(Collections.singletonMap(TipoAcesso.APPLICATION_TYPE_KEY, Collections.singletonList("CLIENTE")));

        when(clientsResource.get(clientId)).thenReturn(clientResource);
        when(clientResource.toRepresentation()).thenReturn(clientRep);
        when(clientResource.getServiceAccountUser()).thenReturn(userRep);
        when(usersResource.get(userId)).thenReturn(userResource);
        when(userResource.toRepresentation()).thenReturn(userRep);
        when(clientApiMapper.mapAttributesToTipo(any())).thenReturn(TipoAcesso.CLIENTE_SISTEMA);

        Optional<ClientApi> result = adapter.findById(clientId);

        assertTrue(result.isPresent());
        assertEquals(clientId, result.get().id());
        verify(clientsResource).get(clientId);
    }

    @Test
    void findById_whenNotFound_shouldReturnEmpty() {
        String clientId = "not-found";
        when(clientsResource.get(clientId)).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        Optional<ClientApi> result = adapter.findById(clientId);

        assertTrue(result.isEmpty());
    }

    @Test
    void findById_whenOtherHttpClientError_shouldThrowException() {
        String clientId = "error-id";
        var serverErrorException = new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
        when(clientsResource.get(clientId)).thenThrow(serverErrorException);

        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> {
            adapter.findById(clientId);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        verify(clientApiMapper, never()).mapAttributesToTipo(any());
    }

    @Test
    void existsByClientId_whenClientExists_shouldReturnTrue() {
        String clientId = "existing-client";
        when(clientsResource.findByClientId(clientId)).thenReturn(List.of(new ClientRepresentation()));

        assertTrue(adapter.existsByClientId(clientId));
    }

    @Test
    void existsByClientId_whenClientDoesNotExist_shouldReturnFalse() {
        String clientId = "non-existing-client";
        when(clientsResource.findByClientId(clientId)).thenReturn(Collections.emptyList());

        assertFalse(adapter.existsByClientId(clientId));
    }

    @Test
    void register_shouldCreateClientAndReturnClientApi() throws Exception {
        ClientApiRegister register = new ClientApiRegister("new-client", "New Client", TipoAcesso.CLIENTE_SISTEMA, true);
        ClientRepresentation clientRep = new ClientRepresentation();
        Response response = mock(Response.class);
        URI location = new URI("/admin/realms/realm/clients/new-id");
        ClientResource clientResource = mock(ClientResource.class);
        UserResource userResource = mock(UserResource.class);
        UserRepresentation userRep = new UserRepresentation();
        userRep.setId("user-id");

        when(clientApiMapper.representation(register)).thenReturn(clientRep);
        when(clientsResource.create(any(ClientRepresentation.class))).thenReturn(response);
        when(response.getLocation()).thenReturn(location);
        when(clientsResource.get("new-id")).thenReturn(clientResource);
        when(clientResource.getServiceAccountUser()).thenReturn(userRep);
        when(usersResource.get("user-id")).thenReturn(userResource);
        when(userResource.toRepresentation()).thenReturn(userRep);
        doNothing().when(userResource).update(any(UserRepresentation.class));

        Role role = mock(Role.class);
        when(role.nome()).thenReturn("New Client");
        RoleRepresentation roleRep = new RoleRepresentation();
        when(roleProvider.findAllByTipo(TipoAcesso.CLIENTE_SISTEMA)).thenReturn(List.of(role));
        when(roleMapper.representation(role)).thenReturn(roleRep);
        RoleMappingResource roleMappingResource = mock(RoleMappingResource.class);
        RoleScopeResource roleScopeResource = mock(RoleScopeResource.class);
        when(userResource.roles()).thenReturn(roleMappingResource);
        when(roleMappingResource.realmLevel()).thenReturn(roleScopeResource);

        // Mocking the final findById call
        ClientApiProviderAdapter spyAdapter = spy(adapter);
        ClientApi expectedApi = new ClientApi("new-id", "new-client", "New Client", TipoAcesso.CLIENTE_SISTEMA, true);
        doReturn(Optional.of(expectedApi)).when(spyAdapter).findById("new-id");

        ClientApi result = spyAdapter.register(register);

        assertNotNull(result);
        assertEquals("new-id", result.id());
        verify(clientsResource).create(any(ClientRepresentation.class));
        verify(userResource).update(any(UserRepresentation.class));
        verify(roleScopeResource).add(anyList());
    }

    @Test
    void updateRelacionadoId_shouldUpdateUserAttributes() {
        String clientId = "client-id";
        String relacionadoId = "rel-id";
        ClientResource clientResource = mock(ClientResource.class);
        UserResource userResource = mock(UserResource.class);
        UserRepresentation userRep = new UserRepresentation();
        userRep.setId("user-id");
        userRep.setAttributes(new java.util.HashMap<>());

        when(clientsResource.get(clientId)).thenReturn(clientResource);
        when(clientResource.getServiceAccountUser()).thenReturn(userRep);
        when(usersResource.get("user-id")).thenReturn(userResource);
        when(userResource.toRepresentation()).thenReturn(userRep);

        adapter.updateRelacionadoId(clientId, relacionadoId);

        ArgumentCaptor<UserRepresentation> captor = ArgumentCaptor.forClass(UserRepresentation.class);
        verify(userResource).update(captor.capture());
        assertTrue(captor.getValue().getAttributes().containsKey(TipoAcesso.APPLICATION_RELACIONADO_ID_KEY));
        assertEquals(relacionadoId, captor.getValue().getAttributes().get(TipoAcesso.APPLICATION_RELACIONADO_ID_KEY).get(0));
    }

    @Test
    void update_shouldUpdateClientName() {
        String clientId = "client-id";
        ClientApiUpdate update = new ClientApiUpdate("Updated Name");
        ClientResource clientResource = mock(ClientResource.class);
        ClientRepresentation clientRep = new ClientRepresentation();

        when(clientsResource.get(clientId)).thenReturn(clientResource);
        when(clientResource.toRepresentation()).thenReturn(clientRep);

        adapter.update(clientId, update);

        ArgumentCaptor<ClientRepresentation> captor = ArgumentCaptor.forClass(ClientRepresentation.class);
        verify(clientResource).update(captor.capture());
        assertEquals("Updated Name", captor.getValue().getName());
    }

    @Test
    void ativar_shouldEnableClient() {
        String clientId = "client-id";
        ClientResource clientResource = mock(ClientResource.class);
        ClientRepresentation clientRep = new ClientRepresentation();
        clientRep.setEnabled(false);

        when(clientsResource.get(clientId)).thenReturn(clientResource);
        when(clientResource.toRepresentation()).thenReturn(clientRep);

        adapter.ativar(clientId);

        ArgumentCaptor<ClientRepresentation> captor = ArgumentCaptor.forClass(ClientRepresentation.class);
        verify(clientResource).update(captor.capture());
        assertTrue(captor.getValue().isEnabled());
    }

    @Test
    void inativar_shouldDisableClient() {
        String clientId = "client-id";
        ClientResource clientResource = mock(ClientResource.class);
        ClientRepresentation clientRep = new ClientRepresentation();
        clientRep.setEnabled(true);

        when(clientsResource.get(clientId)).thenReturn(clientResource);
        when(clientResource.toRepresentation()).thenReturn(clientRep);

        adapter.inativar(clientId);

        ArgumentCaptor<ClientRepresentation> captor = ArgumentCaptor.forClass(ClientRepresentation.class);
        verify(clientResource).update(captor.capture());
        assertFalse(captor.getValue().isEnabled());
    }

    @Test
    void delete_shouldRemoveClient() {
        String clientId = "client-id";
        ClientResource clientResource = mock(ClientResource.class);
        when(clientsResource.get(clientId)).thenReturn(clientResource);

        adapter.delete(clientId);

        verify(clientResource).remove();
    }

    @Test
    void regenerateSecret_shouldGenerateNewSecret() {
        String clientId = "client-id";
        ClientResource clientResource = mock(ClientResource.class);
        when(clientsResource.get(clientId)).thenReturn(clientResource);

        adapter.regenerateSecret(clientId);

        verify(clientResource).generateNewSecret();
    }
}
