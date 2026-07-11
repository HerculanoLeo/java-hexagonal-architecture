package com.lodh8.starter.identity.usuario.infra.keycloak;

import com.lodh8.starter.identity.infra.attribute.KeycloakAttributes;
import com.lodh8.starter.identity.usuario.app.port.UsuarioProviderPort;
import com.lodh8.starter.identity.usuario.domain.*;
import com.lodh8.starter.identity.usuario.infra.UsuarioMapper;
import com.lodh8.starter.identity.usuario.infra.keycloak.dtos.RevokeTokenRequest;
import com.lodh8.starter.identity.usuario.infra.keycloak.dtos.TokenRequest;
import com.lodh8.starter.identity.usuario.infra.keycloak.enums.KeycloakRequiredAction;
import com.lodh8.starter.identity.usuario.infra.keycloak.openfeing.KeycloakAuthClient;
import com.lodh8.starter.shared.exceptions.BadRequestException;
import com.lodh8.starter.shared.models.enums.TipoAcesso;
import com.lodh8.starter.shared.utils.TakeLastPathSegment;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.Strings;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UsuarioKeycloakAdapter implements UsuarioProviderPort {

    private final KeycloakAttributes keycloakAttributes;

    private final UsersResource usersResource;

    private final RealmResource realmResource;

    private final UsuarioMapper usuarioMapper;

    private final RolesResource rolesResource;

    private final KeycloakAuthClient keycloakAuthClient;

    @Override
    public Optional<Usuario> findById(String id) {
        try {
            var representation = this.usersResource.get(id).toRepresentation();
            return Optional.ofNullable(usuarioMapper.dto(representation));
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                return Optional.empty();
            }
            throw ex;
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        var op = this.usersResource.search(email);
        return !op.isEmpty();
    }

    @Override
    public Usuario register(UsuarioRegister requestEntity) {
        var requiredActions = new LinkedList<String>();

        var representation = usuarioMapper.representation(requestEntity);

        if (requestEntity.senha() == null) {
            requiredActions.add(KeycloakRequiredAction.UPDATE_PASSWORD.name());
        }

        if (!requestEntity.emailVerified()) {
            requiredActions.add(KeycloakRequiredAction.VERIFY_EMAIL.name());
        }

        try (var response = this.usersResource.create(representation)) {
            var keycloakUserId = TakeLastPathSegment.fromURI(response.getLocation());

            if (null != requestEntity.grupoId()) {
                this.addGroupsById(keycloakUserId, Collections.singleton(requestEntity.grupoId()));
            }

            var userResource = this.usersResource.get(keycloakUserId);

            if (!requiredActions.isEmpty()) {
                var redirectAction = requestEntity.redirectAction();
                userResource.executeActionsEmail(redirectAction.clientId(), redirectAction.redirectUri(), requiredActions);
            }

            var user = userResource.toRepresentation();

            return usuarioMapper.dto(user);
        }
    }

    @Override
    public void ativar(String id) {
        var userResource = this.usersResource.get(id);
        var representation = userResource.toRepresentation();
        representation.setEnabled(Boolean.TRUE);
        userResource.update(representation);
    }

    @Override
    public void inativar(String id) {
        var userResource = this.usersResource.get(id);
        var representation = userResource.toRepresentation();
        representation.setEnabled(Boolean.FALSE);
        userResource.update(representation);
    }

    @Override
    public void delete(String id) {
        this.usersResource.get(id).remove();
    }

    @Override
    public void resetPassword(String id, RedirectAction requestEntity) {
        var userResource = this.usersResource.get(id);
        userResource.executeActionsEmail(
                requestEntity.clientId(),
                requestEntity.redirectUri(),
                Collections.singletonList(KeycloakRequiredAction.UPDATE_PASSWORD.name())
        );
    }

    @Override
    public void changePassword(String id, TrocaSenha requestEntity) {
        var userResource = this.usersResource.get(id);

        var representation = userResource.toRepresentation();

        if (!this.validateCredential(new TokenRequest(
                keycloakAttributes.clientIdUsers(),
                representation.getUsername(),
                requestEntity.senhaAtual(),
                null,
                "password",
                "openid"
        ))) {
            throw new BadRequestException("senha atual inválida");
        }

        var keycloakCredentials = new CredentialRepresentation();
        keycloakCredentials.setTemporary(false);
        keycloakCredentials.setValue(requestEntity.novaSenha());
        representation.setCredentials(Collections.singletonList(keycloakCredentials));

        userResource.update(representation);
    }

    public void invalidateSessions(String id) {
        var sessions = this.usersResource.get(id).getUserSessions();

        for (var session : sessions) {
            realmResource.deleteSession(session.getId(), false);
        }
    }

    @Override
    public void updateAdminRole(String id, TipoAcesso tipo, boolean main) {
        var userResource = this.usersResource.get(id);

        var rolesToRemove = userResource.roles().realmLevel().listAll().stream()
                .filter(roles -> Strings.CI.equals(roles.getName(), tipo.getAdminRole()))
                .toList();
        userResource.roles().realmLevel().remove(rolesToRemove);

        if (main) {
            var rolesToAdd = this.rolesResource.get(tipo.getAdminRole()).toRepresentation();
            userResource.roles().realmLevel().add(Collections.singletonList(rolesToAdd));
        }
    }

    @Override
    public boolean hasAdminRole(String id, TipoAcesso tipo) {
        if (tipo == null || tipo.getAdminRole() == null) {
            return false;
        }

        return this.usersResource.get(id).roles().realmLevel().listAll().stream()
                .anyMatch(role -> Strings.CI.equals(role.getName(), tipo.getAdminRole()));
    }

    @Override
    public void update(String id, UsuarioUpdate requestEntity) {
        var userResource = this.usersResource.get(id);

        var representation = userResource.toRepresentation();
        representation.setFirstName(requestEntity.nome());

        userResource.update(representation);

        if (null != requestEntity.grupoId()) {
            if (requestEntity.grupoId().isBlank()) {
                this.clearGroups(id);
            } else {
                this.addGroupsById(id, Collections.singleton(requestEntity.grupoId()));
            }
        }
    }

    protected void clearGroups(String keycloakId) {
        var userResource = this.usersResource.get(keycloakId);
        userResource.groups().forEach(group -> userResource.leaveGroup(group.getId()));
    }

    protected void addGroupsById(String keycloakId, Collection<String> groupIds) {
        var userResource = this.usersResource.get(keycloakId);
        var actualGroups = userResource.groups();
        actualGroups.stream()
                .filter(actual -> groupIds.stream().noneMatch(groupId -> Objects.equals(actual.getId(), groupId)))
                .forEach(toRemove -> userResource.leaveGroup(toRemove.getId()));
        groupIds.stream()
                .filter(groupId -> actualGroups.stream().noneMatch(actual -> Objects.equals(groupId, actual.getId())))
                .forEach(userResource::joinGroup);
    }

    protected boolean validateCredential(TokenRequest requestEntity) {
        try {
            var tokenResponse = this.keycloakAuthClient.token(requestEntity);

            this.keycloakAuthClient.revoke(RevokeTokenRequest.builder()
                    .clientId(requestEntity.getClientId())
                    .token(tokenResponse.getAccessToken())
                    .tokenTypeHint("access_token")
                    .build());

            this.keycloakAuthClient.revoke(RevokeTokenRequest.builder()
                    .clientId(requestEntity.getClientId())
                    .token(tokenResponse.getRefreshToken())
                    .tokenTypeHint("refresh_token")
                    .build());
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

}
