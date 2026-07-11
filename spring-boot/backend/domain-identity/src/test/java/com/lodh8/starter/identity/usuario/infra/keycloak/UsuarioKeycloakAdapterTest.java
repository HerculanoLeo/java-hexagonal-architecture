package com.lodh8.starter.identity.usuario.infra.keycloak;

import com.lodh8.starter.identity.infra.attribute.KeycloakAttributes;
import com.lodh8.starter.identity.usuario.domain.*;
import com.lodh8.starter.identity.usuario.infra.UsuarioMapper;
import com.lodh8.starter.identity.usuario.infra.keycloak.dtos.OAuthTokenResponse;
import com.lodh8.starter.identity.usuario.infra.keycloak.dtos.RevokeTokenRequest;
import com.lodh8.starter.identity.usuario.infra.keycloak.dtos.TokenRequest;
import com.lodh8.starter.identity.usuario.infra.keycloak.enums.KeycloakRequiredAction;
import com.lodh8.starter.identity.usuario.infra.keycloak.openfeing.KeycloakAuthClient;
import com.lodh8.starter.shared.exceptions.BadRequestException;
import com.lodh8.starter.shared.models.enums.TipoAcesso;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.*;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
@ExtendWith(MockitoExtension.class)
class UsuarioKeycloakAdapterTest {

    private KeycloakAttributes keycloakAttributes;

    private UsersResource usersResource;

    private RealmResource realmResource;

    private UsuarioMapper usuarioMapper;

    private RolesResource rolesResource;

    private KeycloakAuthClient keycloakAuthClient;

    private UsuarioKeycloakAdapter adapter;

    private UserResource userResource;

    private UserRepresentation userRepresentation;

    private RoleMappingResource mockRoleMapping;

    private RoleScopeResource mockRoleScope;

    private RoleResource mockRoleResource;

    private GroupRepresentation mockGroupRepresentation;

    private List<GroupRepresentation> mockGroupRepresentations;

    @BeforeEach
    public void setUp() {
        this.keycloakAttributes = mock(KeycloakAttributes.class);
        this.usersResource = mock(UsersResource.class);
        this.realmResource = mock(RealmResource.class);
        this.usuarioMapper = mock(UsuarioMapper.class);
        this.rolesResource = mock(RolesResource.class);
        this.keycloakAuthClient = mock(KeycloakAuthClient.class);
        this.userResource = mock(UserResource.class);
        this.userRepresentation = new UserRepresentation();
        this.mockRoleMapping = mock(RoleMappingResource.class);
        this.mockRoleScope = mock(RoleScopeResource.class);
        this.mockRoleResource = mock(RoleResource.class);
        this.mockGroupRepresentation = mock(GroupRepresentation.class);
        this.mockGroupRepresentations = spy(List.of(mockGroupRepresentation));

        this.adapter = new UsuarioKeycloakAdapter(
                this.keycloakAttributes,
                this.usersResource,
                this.realmResource,
                this.usuarioMapper,
                this.rolesResource,
                this.keycloakAuthClient
        );
    }

    @Test
    public void test_findById_should_return_usuario_when_found() {
        String userId = "test-id";
        Usuario expectedUsuario = mock(Usuario.class);

        when(this.usersResource.get(userId)).thenReturn(this.userResource);
        when(this.userResource.toRepresentation()).thenReturn(this.userRepresentation);
        when(this.usuarioMapper.dto(this.userRepresentation)).thenReturn(expectedUsuario);

        Optional<Usuario> result = this.adapter.findById(userId);

        assertTrue(result.isPresent());
        assertEquals(expectedUsuario, result.get());
        verify(this.usersResource, times(1)).get(userId);
        verify(this.usuarioMapper, times(1)).dto(this.userRepresentation);
    }

    @Test
    public void test_findById_should_return_empty_when_not_found_exception() {
        String userId = "not-found-id";

        var notFoundException = new HttpClientErrorException(HttpStatus.NOT_FOUND);
        when(this.usersResource.get(userId)).thenThrow(notFoundException);

        Optional<Usuario> result = this.adapter.findById(userId);

        assertTrue(result.isEmpty());
        verify(this.usuarioMapper, never()).dto(any());
    }

    @Test
    public void test_findById_should_rethrow_exception_when_not_404() {
        String userId = "error-id";

        var serverErrorException = new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
        when(this.usersResource.get(userId)).thenThrow(serverErrorException);

        HttpClientErrorException thrown = assertThrows(HttpClientErrorException.class, () -> {
            this.adapter.findById(userId);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown.getStatusCode());
        verify(this.usuarioMapper, never()).dto(any());
    }

    @Test
    public void test_existsByEmail_should_return_true_when_found() {
        String email = "test@example.com";
        List<UserRepresentation> results = Collections.singletonList(this.userRepresentation);

        when(this.usersResource.search(email)).thenReturn(results);

        boolean exists = this.adapter.existsByEmail(email);

        assertTrue(exists);
        verify(this.usersResource, times(1)).search(email);
    }

    @Test
    public void test_existsByEmail_should_return_false_when_not_found() {
        String email = "test@example.com";
        List<UserRepresentation> results = Collections.emptyList();

        when(this.usersResource.search(email)).thenReturn(results);

        boolean exists = this.adapter.existsByEmail(email);

        assertFalse(exists);
        verify(this.usersResource, times(1)).search(email);
    }

    @Test
    public void test_register_should_create_user_with_actions_and_group() throws URISyntaxException {
        String groupId = "group-123";
        String clientId = "client-abc";
        String redirectUri = "http://localhost";

        RedirectAction mockRedirectAction = mock(RedirectAction.class);
        when(mockRedirectAction.clientId()).thenReturn(clientId);
        when(mockRedirectAction.redirectUri()).thenReturn(redirectUri);

        UsuarioRegister mockRequest = mock(UsuarioRegister.class);
        when(mockRequest.senha()).thenReturn(null);
        when(mockRequest.emailVerified()).thenReturn(false);
        when(mockRequest.grupoId()).thenReturn(groupId);
        when(mockRequest.redirectAction()).thenReturn(mockRedirectAction);

        UserRepresentation repInput = new UserRepresentation();
        UserRepresentation repOutput = new UserRepresentation();
        Usuario mockUsuarioFinal = mock(Usuario.class);

        when(this.usuarioMapper.representation(mockRequest)).thenReturn(repInput);
        when(this.usuarioMapper.dto(repOutput)).thenReturn(mockUsuarioFinal);

        String newUserId = "user-id-456";
        Response mockResponse = mock(Response.class);
        URI locationUri = new URI("https://auth.server.com/admin/realms/realm/users/" + newUserId);

        when(this.usersResource.create(repInput)).thenReturn(mockResponse);
        when(mockResponse.getLocation()).thenReturn(locationUri);

        List<GroupRepresentation> groupRepresentations = Collections.emptyList();

        when(this.usersResource.get(newUserId)).thenReturn(this.userResource);
        when(this.userResource.groups()).thenReturn(groupRepresentations);
        doNothing().when(this.userResource).joinGroup(groupId);

        List<String> expectedActions = List.of(
                KeycloakRequiredAction.UPDATE_PASSWORD.name(),
                KeycloakRequiredAction.VERIFY_EMAIL.name()
        );
        doNothing().when(this.userResource).executeActionsEmail(clientId, redirectUri, expectedActions);

        when(this.userResource.toRepresentation()).thenReturn(repOutput);

        Usuario result = this.adapter.register(mockRequest);

        assertNotNull(result);
        assertEquals(mockUsuarioFinal, result);

        verify(this.usersResource, times(1)).create(repInput);
        verify(mockResponse, times(1)).close();
        verify(this.userResource, times(1)).groups();
        verify(this.userResource, times(1)).joinGroup(groupId);
        verify(this.userResource, never()).leaveGroup(anyString());
        verify(this.userResource, times(1)).executeActionsEmail(clientId, redirectUri, expectedActions);
        verify(this.userResource, times(1)).toRepresentation();
        verify(this.usuarioMapper, times(1)).dto(repOutput);
        verify(this.usersResource, times(2)).get(newUserId);
    }

    @Test
    public void test_register_should_create_user_without_actions_and_group() throws URISyntaxException {
        UsuarioRegister mockRequest = mock(UsuarioRegister.class);
        when(mockRequest.senha()).thenReturn("ValidPassword123@");
        when(mockRequest.emailVerified()).thenReturn(true);
        when(mockRequest.grupoId()).thenReturn(null);

        UserRepresentation repInput = new UserRepresentation();
        UserRepresentation repOutput = new UserRepresentation();
        Usuario mockUsuarioFinal = mock(Usuario.class);

        when(this.usuarioMapper.representation(mockRequest)).thenReturn(repInput);
        when(this.usuarioMapper.dto(repOutput)).thenReturn(mockUsuarioFinal);

        String newUserId = "user-id-789";
        Response mockResponse = mock(Response.class);
        URI locationUri = new URI("https://auth.server.com/admin/realms/realm/users/" + newUserId);

        when(this.usersResource.create(repInput)).thenReturn(mockResponse);
        when(mockResponse.getLocation()).thenReturn(locationUri);

        when(this.usersResource.get(newUserId)).thenReturn(this.userResource);
        when(this.userResource.toRepresentation()).thenReturn(repOutput);

        Usuario result = this.adapter.register(mockRequest);

        assertNotNull(result);
        assertEquals(mockUsuarioFinal, result);

        verify(this.usersResource, times(1)).create(repInput);
        verify(mockResponse, times(1)).close();

        verify(this.userResource, never()).groups();
        verify(this.userResource, never()).joinGroup(anyString());
        verify(this.userResource, never()).executeActionsEmail(any(), any(), any());

        verify(this.usersResource, times(1)).get(newUserId);
        verify(this.userResource, times(1)).toRepresentation();
        verify(this.usuarioMapper, times(1)).dto(repOutput);
    }

    @Test
    public void test_update_should_update_name_and_add_group() {
        String userId = "user-to-update";
        String newName = "Novo Nome";
        String groupId = "group-abc";

        UsuarioUpdate mockRequest = mock(UsuarioUpdate.class);
        when(mockRequest.nome()).thenReturn(newName);
        when(mockRequest.grupoId()).thenReturn(groupId);

        when(this.usersResource.get(userId)).thenReturn(this.userResource);
        when(this.userResource.toRepresentation()).thenReturn(this.userRepresentation);
        doNothing().when(this.userResource).update(any(UserRepresentation.class));

        List<GroupRepresentation> groupRepresentations = Collections.emptyList();
        when(this.userResource.groups()).thenReturn(groupRepresentations);
        doNothing().when(this.userResource).joinGroup(groupId);

        this.adapter.update(userId, mockRequest);

        ArgumentCaptor<UserRepresentation> representationCaptor = ArgumentCaptor.forClass(UserRepresentation.class);
        verify(this.userResource, times(1)).update(representationCaptor.capture());
        assertEquals(newName, representationCaptor.getValue().getFirstName());

        verify(this.usersResource, times(2)).get(userId);

        verify(this.userResource, times(1)).groups();
        verify(this.userResource, times(1)).joinGroup(groupId);
        verify(this.userResource, never()).leaveGroup(anyString());
    }

    @Test
    public void test_update_should_update_name_only_when_group_is_null() {
        String userId = "user-to-update";
        String newName = "Apenas Nome";

        UsuarioUpdate mockRequest = mock(UsuarioUpdate.class);
        when(mockRequest.nome()).thenReturn(newName);
        when(mockRequest.grupoId()).thenReturn(null);

        when(this.usersResource.get(userId)).thenReturn(this.userResource);
        when(this.userResource.toRepresentation()).thenReturn(this.userRepresentation);
        doNothing().when(this.userResource).update(any(UserRepresentation.class));

        this.adapter.update(userId, mockRequest);

        ArgumentCaptor<UserRepresentation> representationCaptor = ArgumentCaptor.forClass(UserRepresentation.class);
        verify(this.userResource, times(1)).update(representationCaptor.capture());
        assertEquals(newName, representationCaptor.getValue().getFirstName());

        verify(this.usersResource, times(1)).get(userId);

        verify(this.userResource, never()).groups();
        verify(this.userResource, never()).joinGroup(anyString());
    }

    @Test
    public void test_delete_should_call_remove() {
        String userId = "delete-id";

        when(this.usersResource.get(userId)).thenReturn(this.userResource);
        doNothing().when(this.userResource).remove();

        this.adapter.delete(userId);

        verify(this.usersResource, times(1)).get(userId);
        verify(this.userResource, times(1)).remove();
    }

    @Test
    public void test_resetPassword_should_call_executeActionsEmail() {
        String userId = "user-id-123";
        String clientId = "my-client";
        String redirectUri = "https://example.com/redirect";

        RedirectAction mockRequest = mock(RedirectAction.class);
        when(mockRequest.clientId()).thenReturn(clientId);
        when(mockRequest.redirectUri()).thenReturn(redirectUri);

        when(this.usersResource.get(userId)).thenReturn(this.userResource);

        List<String> expectedActions = Collections.singletonList(KeycloakRequiredAction.UPDATE_PASSWORD.name());

        doNothing().when(this.userResource).executeActionsEmail(
                eq(clientId),
                eq(redirectUri),
                eq(expectedActions)
        );

        this.adapter.resetPassword(userId, mockRequest);

        verify(this.usersResource, times(1)).get(userId);
        verify(this.userResource, times(1)).executeActionsEmail(
                clientId,
                redirectUri,
                expectedActions
        );
    }

    @Test
    public void test_changePassword_should_update_when_credential_is_valid() {
        String userId = "user-id";
        String oldPass = "oldPass123";
        String newPass = "newPass456!";
        String clientId = "user-client";
        String username = "test-user";
        String accessToken = "access-token-xyz";
        String refreshToken = "refresh-token-abc";

        TrocaSenha mockRequest = mock(TrocaSenha.class);
        when(mockRequest.senhaAtual()).thenReturn(oldPass);
        when(mockRequest.novaSenha()).thenReturn(newPass);

        when(this.usersResource.get(userId)).thenReturn(this.userResource);
        when(this.userResource.toRepresentation()).thenReturn(this.userRepresentation);

        when(this.keycloakAttributes.clientIdUsers()).thenReturn(clientId);

        OAuthTokenResponse mockTokenResponse = mock(OAuthTokenResponse.class);
        when(mockTokenResponse.getAccessToken()).thenReturn(accessToken);
        when(mockTokenResponse.getRefreshToken()).thenReturn(refreshToken);

        when(this.keycloakAuthClient.token(any(TokenRequest.class))).thenReturn(mockTokenResponse);
        when(this.keycloakAuthClient.revoke(any(RevokeTokenRequest.class))).thenReturn(ResponseEntity.noContent().build());

        doNothing().when(this.userResource).update(any(UserRepresentation.class));

        this.adapter.changePassword(userId, mockRequest);

        verify(this.keycloakAuthClient, times(1)).token(any(TokenRequest.class));
        verify(this.keycloakAuthClient, times(2)).revoke(any(RevokeTokenRequest.class));

        ArgumentCaptor<UserRepresentation> repCaptor = ArgumentCaptor.forClass(UserRepresentation.class);
        verify(this.userResource, times(1)).update(repCaptor.capture());

        UserRepresentation capturedRep = repCaptor.getValue();
        assertEquals(1, capturedRep.getCredentials().size());
        CredentialRepresentation cred = capturedRep.getCredentials().get(0);
        assertEquals(newPass, cred.getValue());
        assertFalse(cred.isTemporary());
    }

    @Test
    public void test_changePassword_should_throw_exception_when_credential_is_invalid() {
        String userId = "user-id";
        String wrongOldPass = "wrongPass123";
        String clientId = "user-client";
        String username = "test-user";

        TrocaSenha mockRequest = mock(TrocaSenha.class);
        when(mockRequest.senhaAtual()).thenReturn(wrongOldPass);

        this.userRepresentation.setUsername(username);
        when(this.usersResource.get(userId)).thenReturn(this.userResource);
        when(this.userResource.toRepresentation()).thenReturn(this.userRepresentation);

        when(this.keycloakAttributes.clientIdUsers()).thenReturn(clientId);

        when(this.keycloakAuthClient.token(any(TokenRequest.class)))
                .thenThrow(new RuntimeException("Simulated auth failure"));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            this.adapter.changePassword(userId, mockRequest);
        });

        assertEquals("senha atual inválida", exception.getMessage());
    }

    @Test
    public void test_invalidateSessions_should_delete_each_session() {
        String userId = "user-id-sessions";
        String sessionId1 = "session-1";
        String sessionId2 = "session-2";

        UserSessionRepresentation session1 = mock(UserSessionRepresentation.class);
        when(session1.getId()).thenReturn(sessionId1);

        UserSessionRepresentation session2 = mock(UserSessionRepresentation.class);
        when(session2.getId()).thenReturn(sessionId2);

        List<UserSessionRepresentation> sessionList = List.of(session1, session2);

        when(this.usersResource.get(userId)).thenReturn(this.userResource);
        when(this.userResource.getUserSessions()).thenReturn(sessionList);

        doNothing().when(this.realmResource).deleteSession(anyString(), eq(false));

        this.adapter.invalidateSessions(userId);

        verify(this.usersResource, times(1)).get(userId);
        verify(this.userResource, times(1)).getUserSessions();

        verify(this.realmResource, times(1)).deleteSession(sessionId1, false);
        verify(this.realmResource, times(1)).deleteSession(sessionId2, false);

        verify(this.realmResource, times(sessionList.size())).deleteSession(anyString(), eq(false));
    }

    @Test
    public void test_invalidateSessions_should_do_nothing_when_session_list_is_empty() {
        String userId = "user-id-no-sessions";

        List<UserSessionRepresentation> emptySessionList = Collections.emptyList();

        when(this.usersResource.get(userId)).thenReturn(this.userResource);
        when(this.userResource.getUserSessions()).thenReturn(emptySessionList);

        this.adapter.invalidateSessions(userId);

        verify(this.usersResource, times(1)).get(userId);
        verify(this.userResource, times(1)).getUserSessions();

        verify(this.realmResource, never()).deleteSession(anyString(), anyBoolean());
    }


    @Test
    public void test_updateAdminRole_should_remove_and_add_role_when_main_is_true() {
        String userId = "user-id-1";
        String roleName = "ROLE_ADMIN_TEST";

        TipoAcesso mockTipo = mock(TipoAcesso.class);
        when(mockTipo.getAdminRole()).thenReturn(roleName);

        when(this.usersResource.get(userId)).thenReturn(this.userResource);
        when(this.userResource.roles()).thenReturn(this.mockRoleMapping);
        when(this.mockRoleMapping.realmLevel()).thenReturn(this.mockRoleScope);

        RoleRepresentation roleToRemove = new RoleRepresentation(roleName, "desc", false);
        RoleRepresentation otherRole = new RoleRepresentation("OTHER_ROLE", "desc", false);
        List<RoleRepresentation> existingRoles = List.of(roleToRemove, otherRole);

        when(this.mockRoleScope.listAll()).thenReturn(existingRoles);
        doNothing().when(this.mockRoleScope).remove(anyList());

        RoleRepresentation roleToAdd = new RoleRepresentation(roleName, "new desc", false);

        when(this.rolesResource.get(roleName)).thenReturn(this.mockRoleResource);
        when(this.mockRoleResource.toRepresentation()).thenReturn(roleToAdd);
        doNothing().when(this.mockRoleScope).add(anyList());

        this.adapter.updateAdminRole(userId, mockTipo, true);


        verify(this.mockRoleScope, times(1)).listAll();
        ArgumentCaptor<List<RoleRepresentation>> removeCaptor = ArgumentCaptor.forClass(List.class);
        verify(this.mockRoleScope, times(1)).remove(removeCaptor.capture());
        assertEquals(1, removeCaptor.getValue().size());

        verify(this.rolesResource, times(1)).get(roleName);
        verify(this.mockRoleResource, times(1)).toRepresentation();
        ArgumentCaptor<List<RoleRepresentation>> addCaptor = ArgumentCaptor.forClass(List.class);
        verify(this.mockRoleScope, times(1)).add(addCaptor.capture());
        assertEquals(1, addCaptor.getValue().size());
        assertEquals(roleToAdd, addCaptor.getValue().get(0));
    }

    @Test
    public void test_updateAdminRole_should_only_remove_role_when_main_is_false() {
        String userId = "user-id-2";
        String roleName = "ROLE_ADMIN_TEST";

        TipoAcesso mockTipo = mock(TipoAcesso.class);
        when(mockTipo.getAdminRole()).thenReturn(roleName);

        when(this.usersResource.get(userId)).thenReturn(this.userResource);
        when(this.userResource.roles()).thenReturn(this.mockRoleMapping);
        when(this.mockRoleMapping.realmLevel()).thenReturn(this.mockRoleScope);

        RoleRepresentation roleToRemove = new RoleRepresentation(roleName, "desc", false);
        List<RoleRepresentation> existingRoles = List.of(roleToRemove);

        when(this.mockRoleScope.listAll()).thenReturn(existingRoles);
        doNothing().when(this.mockRoleScope).remove(anyList());

        this.adapter.updateAdminRole(userId, mockTipo, false);

        verify(this.mockRoleScope, times(1)).listAll();
        verify(this.mockRoleScope, times(1)).remove(anyList());

        verify(this.rolesResource, never()).get(anyString());
        verify(this.mockRoleResource, never()).toRepresentation();
        verify(this.mockRoleScope, never()).add(anyList());
    }

    @Test
    public void test_hasAdminRole_should_return_true_when_user_has_admin_role() {
        String userId = "user-id-main";
        String roleName = "admin-sistemas";

        TipoAcesso mockTipo = mock(TipoAcesso.class);
        when(mockTipo.getAdminRole()).thenReturn(roleName);

        when(this.usersResource.get(userId)).thenReturn(this.userResource);
        when(this.userResource.roles()).thenReturn(this.mockRoleMapping);
        when(this.mockRoleMapping.realmLevel()).thenReturn(this.mockRoleScope);
        when(this.mockRoleScope.listAll()).thenReturn(List.of(
                new RoleRepresentation(roleName, "desc", false),
                new RoleRepresentation("other", "desc", false)
        ));

        assertTrue(this.adapter.hasAdminRole(userId, mockTipo));
    }

    @Test
    public void test_hasAdminRole_should_return_false_when_user_lacks_admin_role() {
        String userId = "user-id-regular";
        String roleName = "admin-sistemas";

        TipoAcesso mockTipo = mock(TipoAcesso.class);
        when(mockTipo.getAdminRole()).thenReturn(roleName);

        when(this.usersResource.get(userId)).thenReturn(this.userResource);
        when(this.userResource.roles()).thenReturn(this.mockRoleMapping);
        when(this.mockRoleMapping.realmLevel()).thenReturn(this.mockRoleScope);
        when(this.mockRoleScope.listAll()).thenReturn(List.of(
                new RoleRepresentation("usuarios-sistemas", "desc", false)
        ));

        assertFalse(this.adapter.hasAdminRole(userId, mockTipo));
    }

    @Test
    public void test_ativar_should_update_user_to_enabled() {
        String userId = "user-id";
        this.userRepresentation.setEnabled(false);

        when(this.usersResource.get(userId)).thenReturn(this.userResource);
        when(this.userResource.toRepresentation()).thenReturn(this.userRepresentation);
        doNothing().when(this.userResource).update(any(UserRepresentation.class));

        this.adapter.ativar(userId);

        verify(this.userResource, times(1)).update(this.userRepresentation);
        assertTrue(this.userRepresentation.isEnabled());
    }

    @Test
    public void test_inativar_should_update_user_to_disabled() {
        String userId = "user-id";
        this.userRepresentation.setEnabled(true);

        when(this.usersResource.get(userId)).thenReturn(this.userResource);
        when(this.userResource.toRepresentation()).thenReturn(this.userRepresentation);
        doNothing().when(this.userResource).update(any(UserRepresentation.class));

        this.adapter.inativar(userId);

        verify(this.userResource, times(1)).update(this.userRepresentation);
        assertFalse(this.userRepresentation.isEnabled());
    }

    @Test
    public void test_addGroupsById_should_add_new_group() {
        String userId = "user-id-1";
        String groupToAddId = "group-new";
        Collection<String> desiredGroupIds = Collections.singletonList(groupToAddId);
        List<GroupRepresentation> groupRepresentations = spy(Collections.emptyList());

        when(this.usersResource.get(userId)).thenReturn(this.userResource);
        when(this.userResource.groups()).thenReturn(groupRepresentations);

        doNothing().when(this.userResource).joinGroup(groupToAddId);

        this.adapter.addGroupsById(userId, desiredGroupIds);

        verify(this.userResource, times(1)).groups();
        verify(groupRepresentations, times(2)).stream();

        verify(this.userResource, times(1)).joinGroup(groupToAddId);
        verify(this.userResource, never()).leaveGroup(anyString());
    }

    @Test
    public void test_addGroupsById_should_remove_old_group() {
        String userId = "user-id-2";
        String groupToRemoveId = "group-old";
        Collection<String> desiredGroupIds = Collections.emptyList();
        GroupRepresentation groupRepresentation = mock(GroupRepresentation.class);
        List<GroupRepresentation> groupRepresentations = spy(Collections.singletonList(groupRepresentation));

        GroupRepresentation groupToRemoveRep = mock(GroupRepresentation.class);
        when(groupToRemoveRep.getId()).thenReturn(groupToRemoveId);

        when(this.usersResource.get(userId)).thenReturn(this.userResource);
        when(this.userResource.groups()).thenReturn(groupRepresentations);

        when(groupRepresentations.stream()).thenReturn(Stream.of(groupToRemoveRep));

        doNothing().when(this.userResource).leaveGroup(groupToRemoveId);

        this.adapter.addGroupsById(userId, desiredGroupIds);

        verify(this.userResource, times(1)).groups();

        verify(this.userResource, times(1)).leaveGroup(groupToRemoveId);
        verify(this.userResource, never()).joinGroup(anyString());
    }

    @Test
    public void test_addGroupsById_should_sync_add_and_remove() {
        String userId = "user-id-3";
        String groupToRemoveId = "group-old";
        String groupToAddId = "group-new";
        Collection<String> desiredGroupIds = Collections.singletonList(groupToAddId);

        GroupRepresentation groupToRemoveRep = mock(GroupRepresentation.class);
        when(groupToRemoveRep.getId()).thenReturn(groupToRemoveId);

        when(this.usersResource.get(userId)).thenReturn(this.userResource);
        when(this.userResource.groups()).thenReturn(this.mockGroupRepresentations);

        when(this.mockGroupRepresentations.stream())
                .thenReturn(Stream.of(groupToRemoveRep))
                .thenReturn(Stream.of(groupToRemoveRep));

        doNothing().when(this.userResource).leaveGroup(groupToRemoveId);
        doNothing().when(this.userResource).joinGroup(groupToAddId);

        this.adapter.addGroupsById(userId, desiredGroupIds);

        verify(this.userResource, times(1)).groups();

        verify(this.userResource, times(1)).leaveGroup(groupToRemoveId);
        verify(this.userResource, times(1)).joinGroup(groupToAddId);
    }


    @Test
    public void test_validateCredential_should_return_true_when_auth_is_successful() {
        String clientId = "client-id";
        String accessToken = "access-token";
        String refreshToken = "refresh-token";

        TokenRequest mockRequest = mock(TokenRequest.class);
        when(mockRequest.getClientId()).thenReturn(clientId);

        OAuthTokenResponse mockResponse = mock(OAuthTokenResponse.class);
        when(mockResponse.getAccessToken()).thenReturn(accessToken);
        when(mockResponse.getRefreshToken()).thenReturn(refreshToken);

        when(this.keycloakAuthClient.token(mockRequest)).thenReturn(mockResponse);

        when(this.keycloakAuthClient.revoke(any(RevokeTokenRequest.class))).thenReturn(ResponseEntity.noContent().build());

        boolean result = this.adapter.validateCredential(mockRequest);

        assertTrue(result);

        verify(this.keycloakAuthClient, times(1)).token(mockRequest);

        ArgumentCaptor<RevokeTokenRequest> revokeCaptor = ArgumentCaptor.forClass(RevokeTokenRequest.class);
        verify(this.keycloakAuthClient, times(2)).revoke(revokeCaptor.capture());

        RevokeTokenRequest revoke1 = revokeCaptor.getAllValues().get(0);
        RevokeTokenRequest revoke2 = revokeCaptor.getAllValues().get(1);

        assertEquals(clientId, revoke1.getClientId());
        assertEquals(accessToken, revoke1.getToken());
        assertEquals("access_token", revoke1.getTokenTypeHint());

        assertEquals(clientId, revoke2.getClientId());
        assertEquals(refreshToken, revoke2.getToken());
        assertEquals("refresh_token", revoke2.getTokenTypeHint());
    }

    @Test
    public void test_validateCredential_should_return_false_when_auth_fails() {
        TokenRequest mockRequest = mock(TokenRequest.class);

        when(this.keycloakAuthClient.token(mockRequest))
                .thenThrow(new RuntimeException("Simulated authentication failure"));

        boolean result = this.adapter.validateCredential(mockRequest);

        assertFalse(result);

        verify(this.keycloakAuthClient, times(1)).token(mockRequest);

        verify(this.keycloakAuthClient, never()).revoke(any(RevokeTokenRequest.class));
    }

}