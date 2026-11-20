package com.lodh8.starter.identity.clientapi.infra.keycloak;

import com.lodh8.starter.identity.clientapi.app.port.ClientApiProviderPort;
import com.lodh8.starter.identity.clientapi.domain.ClientApi;
import com.lodh8.starter.identity.clientapi.domain.ClientApiRegister;
import com.lodh8.starter.identity.clientapi.domain.ClientApiUpdate;
import com.lodh8.starter.identity.clientapi.infra.mapper.ClientApiMapper;
import com.lodh8.starter.identity.roles.app.port.RoleProviderPort;
import com.lodh8.starter.identity.roles.infra.mapper.RoleMapper;
import com.lodh8.starter.shared.models.enums.TipoAcesso;
import com.lodh8.starter.shared.utils.IdentificadorLog;
import com.lodh8.starter.shared.utils.TakeLastPathSegment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Strings;
import org.keycloak.admin.client.resource.ClientsResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Collections;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientApiProviderAdapter implements ClientApiProviderPort {

    private final ClientsResource clientsResource;

    private final UsersResource usersResource;

    private final ClientApiMapper clientApiMapper;

    private final RoleProviderPort roleProvider;

    private final RoleMapper roleMapper;

    @Override
    public Optional<ClientApi> findById(String id) {
        log.info("clientApiKeycloak findById inicio id={}", IdentificadorLog.mascarar(id));
        log.debug("clientApiKeycloak findById inicio id={}", id);
        try {
            var clientResource = this.clientsResource.get(id);
            var representation = clientResource.toRepresentation();

            var serviceAccountUser = clientResource.getServiceAccountUser();
            var userResource = this.usersResource.get(serviceAccountUser.getId());
            var userKeycloak = userResource.toRepresentation();
            var tipoAcesso = clientApiMapper.mapAttributesToTipo(userKeycloak.getAttributes());

            log.info("clientApiKeycloak findById fim id={}", IdentificadorLog.mascarar(id));
            return Optional.of(new ClientApi(
                    representation.getId(),
                    representation.getClientId(),
                    representation.getName(),
                    tipoAcesso,
                    representation.isEnabled()
            ));
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                log.warn("clientApiKeycloak findById id={} resultado=nao_encontrado", IdentificadorLog.mascarar(id));
                return Optional.empty();
            }
            log.error("clientApiKeycloak findById id={} erro", IdentificadorLog.mascarar(id), ex);
            throw ex;
        }
    }

    @Override
    public boolean existsByClientId(String clientId) {
        log.info("clientApiKeycloak existsByClientId inicio clientId={}", IdentificadorLog.mascarar(clientId));
        log.debug("clientApiKeycloak existsByClientId inicio clientId={}", clientId);
        var op = clientsResource.findByClientId(clientId);
        var exists = !op.isEmpty();
        log.info("clientApiKeycloak existsByClientId fim clientId={} resultado={}", IdentificadorLog.mascarar(clientId), exists);
        return exists;
    }

    @Override
    public ClientApi register(ClientApiRegister requestEntity) {
        log.info("clientApiKeycloak register inicio clientId={}", IdentificadorLog.mascarar(requestEntity.clientId()));
        log.debug("clientApiKeycloak register inicio clientId={} tipo={}", requestEntity.clientId(), requestEntity.tipo());
        var representation = clientApiMapper.representation(requestEntity);
        representation.setClientAuthenticatorType("client-secret");
        representation.setServiceAccountsEnabled(true);
        representation.setDirectAccessGrantsEnabled(true);

        try (var response = this.clientsResource.create(representation)) {
            var id = TakeLastPathSegment.fromURI(response.getLocation());

            var resource = clientsResource.get(id);

            var serviceAccountUser = resource.getServiceAccountUser();
            var userResource = this.usersResource.get(serviceAccountUser.getId());
            var userRepresentation = userResource.toRepresentation();
            userRepresentation.setAttributes(requestEntity.attributes());
            userResource.update(userRepresentation);

            roleProvider.findAllByTipo(requestEntity.tipo())
                    .stream().filter(r -> Strings.CS.equals(r.nome(), requestEntity.nome()))
                    .findFirst()
                    .ifPresent((role) -> userResource.roles().realmLevel()
                            .add(Collections.singletonList(roleMapper.representation(role))));

            var client = findById(id).orElseThrow();
            log.info("clientApiKeycloak register fim id={}", IdentificadorLog.mascarar(id));
            log.debug("clientApiKeycloak register fim id={}", id);
            return client;
        }
    }

    @Override
    public void updateRelacionadoId(String id, String relacionadoId) {
        log.info(
                "clientApiKeycloak updateRelacionadoId inicio id={} relacionadoId={}",
                IdentificadorLog.mascarar(id),
                IdentificadorLog.mascarar(relacionadoId)
        );
        log.debug("clientApiKeycloak updateRelacionadoId inicio id={} relacionadoId={}", id, relacionadoId);
        var resource = this.clientsResource.get(id);

        var serviceAccountUser = resource.getServiceAccountUser();
        var userResource = this.usersResource.get(serviceAccountUser.getId());
        var userRepresentation = userResource.toRepresentation();
        var attributes = userRepresentation.getAttributes();
        attributes.put(TipoAcesso.APPLICATION_RELACIONADO_ID_KEY, Collections.singletonList(relacionadoId));

        userRepresentation.setAttributes(attributes);
        userResource.update(userRepresentation);
        log.info("clientApiKeycloak updateRelacionadoId fim id={}", IdentificadorLog.mascarar(id));
    }

    @Override
    public void update(String id, ClientApiUpdate requestEntity) {
        log.info("clientApiKeycloak update inicio id={}", IdentificadorLog.mascarar(id));
        log.debug("clientApiKeycloak update inicio id={}", id);
        var clientResource = this.clientsResource.get(id);
        var clientRepresentation = clientResource.toRepresentation();
        clientRepresentation.setName(requestEntity.nome());
        clientResource.update(clientRepresentation);
        log.info("clientApiKeycloak update fim id={}", IdentificadorLog.mascarar(id));
    }

    @Override
    public void ativar(String id) {
        log.info("clientApiKeycloak ativar inicio id={}", IdentificadorLog.mascarar(id));
        var clientResource = this.clientsResource.get(id);
        var client = clientResource.toRepresentation();
        client.setEnabled(Boolean.TRUE);
        clientResource.update(client);
        log.info("clientApiKeycloak ativar fim id={}", IdentificadorLog.mascarar(id));

    }

    @Override
    public void inativar(String id) {
        log.info("clientApiKeycloak inativar inicio id={}", IdentificadorLog.mascarar(id));
        var clientResource = this.clientsResource.get(id);
        var client = clientResource.toRepresentation();
        client.setEnabled(Boolean.FALSE);
        clientResource.update(client);
        log.info("clientApiKeycloak inativar fim id={}", IdentificadorLog.mascarar(id));
    }

    @Override
    public void delete(String id) {
        log.info("clientApiKeycloak delete inicio id={}", IdentificadorLog.mascarar(id));
        clientsResource.get(id).remove();
        log.info("clientApiKeycloak delete fim id={}", IdentificadorLog.mascarar(id));
    }

    @Override
    public void regenerateSecret(String id) {
        log.info("clientApiKeycloak regenerateSecret inicio id={}", IdentificadorLog.mascarar(id));
        var clientResource = this.clientsResource.get(id);
        clientResource.generateNewSecret();
        log.info("clientApiKeycloak regenerateSecret fim id={}", IdentificadorLog.mascarar(id));
    }

}