package com.lodh8.starter.identity.usuario.infra.keycloak;

import com.lodh8.starter.identity.domain.attributes.KeycloakAttributes;
import com.lodh8.starter.identity.usuario.app.port.UsuarioProviderPort;
import com.lodh8.starter.identity.usuario.domain.*;
import com.lodh8.starter.identity.usuario.infra.keycloak.dtos.RevokeTokenRequest;
import com.lodh8.starter.identity.usuario.infra.keycloak.dtos.TokenRequest;
import com.lodh8.starter.identity.usuario.infra.keycloak.enums.KeycloakRequiredAction;
import com.lodh8.starter.identity.usuario.infra.keycloak.openfeing.KeycloakAuthClient;
import com.lodh8.starter.identity.usuario.infra.mapper.UsuarioMapper;
import com.lodh8.starter.shared.exceptions.BadRequestException;
import com.lodh8.starter.shared.models.enums.TipoAcesso;
import com.lodh8.starter.shared.utils.IdentificadorLog;
import com.lodh8.starter.shared.utils.TakeLastPathSegment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Strings;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.*;

@Slf4j
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
        log.info("usuarioKeycloak findById inicio id={}", IdentificadorLog.mascarar(id));
        log.debug("usuarioKeycloak findById inicio id={}", id);
        try {
            var representation = this.usersResource.get(id).toRepresentation();
            log.info("usuarioKeycloak findById fim id={}", IdentificadorLog.mascarar(id));
            return Optional.ofNullable(usuarioMapper.dto(representation));
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                log.warn("usuarioKeycloak findById id={} resultado=nao_encontrado", IdentificadorLog.mascarar(id));
                return Optional.empty();
            }
            log.error("usuarioKeycloak findById id={} erro", IdentificadorLog.mascarar(id), ex);
            throw ex;
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        log.info("usuarioKeycloak existsByEmail inicio email={}", IdentificadorLog.mascarar(email));
        log.debug("usuarioKeycloak existsByEmail inicio email={}", email);
        var op = this.usersResource.search(email);
        var exists = !op.isEmpty();
        log.info("usuarioKeycloak existsByEmail fim email={} resultado={}", IdentificadorLog.mascarar(email), exists);
        return exists;
    }

    @Override
    public Usuario register(UsuarioRegister requestEntity) {
        log.info("usuarioKeycloak register inicio email={}", IdentificadorLog.mascarar(requestEntity.email()));
        log.debug("usuarioKeycloak register inicio email={} tipo={}", requestEntity.email(), requestEntity.tipo());
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

            var usuario = usuarioMapper.dto(user);
            log.info("usuarioKeycloak register fim id={}", IdentificadorLog.mascarar(keycloakUserId));
            log.debug("usuarioKeycloak register fim id={}", keycloakUserId);
            return usuario;
        }
    }

    @Override
    public void ativar(String id) {
        log.info("usuarioKeycloak ativar inicio id={}", IdentificadorLog.mascarar(id));
        log.debug("usuarioKeycloak ativar inicio id={}", id);
        var userResource = this.usersResource.get(id);
        var representation = userResource.toRepresentation();
        representation.setEnabled(Boolean.TRUE);
        userResource.update(representation);
        log.info("usuarioKeycloak ativar fim id={}", IdentificadorLog.mascarar(id));
    }

    @Override
    public void inativar(String id) {
        log.info("usuarioKeycloak inativar inicio id={}", IdentificadorLog.mascarar(id));
        log.debug("usuarioKeycloak inativar inicio id={}", id);
        var userResource = this.usersResource.get(id);
        var representation = userResource.toRepresentation();
        representation.setEnabled(Boolean.FALSE);
        userResource.update(representation);
        log.info("usuarioKeycloak inativar fim id={}", IdentificadorLog.mascarar(id));
    }

    @Override
    public void delete(String id) {
        log.info("usuarioKeycloak delete inicio id={}", IdentificadorLog.mascarar(id));
        log.debug("usuarioKeycloak delete inicio id={}", id);
        this.usersResource.get(id).remove();
        log.info("usuarioKeycloak delete fim id={}", IdentificadorLog.mascarar(id));
    }

    @Override
    public void resetPassword(String id, RedirectAction requestEntity) {
        log.info("usuarioKeycloak resetPassword inicio id={}", IdentificadorLog.mascarar(id));
        log.debug("usuarioKeycloak resetPassword inicio id={}", id);
        var userResource = this.usersResource.get(id);
        userResource.executeActionsEmail(
                requestEntity.clientId(),
                requestEntity.redirectUri(),
                Collections.singletonList(KeycloakRequiredAction.UPDATE_PASSWORD.name())
        );
        log.info("usuarioKeycloak resetPassword fim id={}", IdentificadorLog.mascarar(id));
    }

    @Override
    public void changePassword(String id, TrocaSenha requestEntity) {
        log.info("usuarioKeycloak changePassword inicio id={}", IdentificadorLog.mascarar(id));
        log.debug("usuarioKeycloak changePassword inicio id={}", id);
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
            log.warn("usuarioKeycloak changePassword id={} resultado=senha_atual_invalida", IdentificadorLog.mascarar(id));
            throw new BadRequestException("senha atual inválida");
        }

        var keycloakCredentials = new CredentialRepresentation();
        keycloakCredentials.setTemporary(false);
        keycloakCredentials.setValue(requestEntity.novaSenha());
        representation.setCredentials(Collections.singletonList(keycloakCredentials));

        userResource.update(representation);
        log.info("usuarioKeycloak changePassword fim id={}", IdentificadorLog.mascarar(id));
    }

    public void invalidateSessions(String id) {
        log.info("usuarioKeycloak invalidateSessions inicio id={}", IdentificadorLog.mascarar(id));
        log.debug("usuarioKeycloak invalidateSessions inicio id={}", id);
        var sessions = this.usersResource.get(id).getUserSessions();

        for (var session : sessions) {
            realmResource.deleteSession(session.getId(), false);
        }
        log.info("usuarioKeycloak invalidateSessions fim id={} total={}", IdentificadorLog.mascarar(id), sessions.size());
    }

    @Override
    public void updateAdminRole(String id, TipoAcesso tipo, boolean main) {
        log.info("usuarioKeycloak updateAdminRole inicio id={} tipo={} main={}", IdentificadorLog.mascarar(id), tipo, main);
        var userResource = this.usersResource.get(id);

        var rolesToRemove = userResource.roles().realmLevel().listAll().stream()
                .filter(roles -> Strings.CI.equals(roles.getName(), tipo.getAdminRole()))
                .toList();
        userResource.roles().realmLevel().remove(rolesToRemove);

        if (main) {
            var rolesToAdd = this.rolesResource.get(tipo.getAdminRole()).toRepresentation();
            userResource.roles().realmLevel().add(Collections.singletonList(rolesToAdd));
        }
        log.info("usuarioKeycloak updateAdminRole fim id={}", IdentificadorLog.mascarar(id));
    }

    @Override
    public boolean hasAdminRole(String id, TipoAcesso tipo) {
        log.info("usuarioKeycloak hasAdminRole inicio id={} tipo={}", IdentificadorLog.mascarar(id), tipo);
        if (tipo == null || tipo.getAdminRole() == null) {
            log.info("usuarioKeycloak hasAdminRole fim id={} resultado=false", IdentificadorLog.mascarar(id));
            return false;
        }

        var has = this.usersResource.get(id).roles().realmLevel().listAll().stream()
                .anyMatch(role -> Strings.CI.equals(role.getName(), tipo.getAdminRole()));
        log.info("usuarioKeycloak hasAdminRole fim id={} resultado={}", IdentificadorLog.mascarar(id), has);
        return has;
    }

    @Override
    public void update(String id, UsuarioUpdate requestEntity) {
        log.info("usuarioKeycloak update inicio id={}", IdentificadorLog.mascarar(id));
        log.debug("usuarioKeycloak update inicio id={}", id);
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
        log.info("usuarioKeycloak update fim id={}", IdentificadorLog.mascarar(id));
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