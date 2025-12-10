package com.herculanoleo.starter.identity.clientapi.infra.keycloak;

import com.herculanoleo.starter.identity.clientapi.app.port.ClientApiProviderPort;
import com.herculanoleo.starter.identity.clientapi.domain.ClientApi;
import com.herculanoleo.starter.identity.clientapi.domain.ClientApiRegister;
import com.herculanoleo.starter.identity.clientapi.domain.ClientApiUpdate;
import com.herculanoleo.starter.identity.clientapi.infra.ClientApiMapper;
import com.herculanoleo.starter.identity.roles.app.port.RoleProviderPort;
import com.herculanoleo.starter.identity.roles.infra.RoleMapper;
import com.herculanoleo.starter.shared.models.enums.TipoAcesso;
import com.herculanoleo.starter.shared.utils.TakeLastPathSegment;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.Strings;
import org.keycloak.admin.client.resource.ClientsResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Collections;
import java.util.Optional;

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
        try {
            var clientResource = this.clientsResource.get(id);
            var representation = clientResource.toRepresentation();

            var serviceAccountUser = clientResource.getServiceAccountUser();
            var userResource = this.usersResource.get(serviceAccountUser.getId());
            var userKeycloak = userResource.toRepresentation();
            var tipoAcesso = clientApiMapper.mapAttributesToTipo(userKeycloak.getAttributes());

            return Optional.of(new ClientApi(
                    representation.getId(),
                    representation.getClientId(),
                    representation.getName(),
                    tipoAcesso,
                    representation.isEnabled()
            ));
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                return Optional.empty();
            }
            throw ex;
        }
    }

    @Override
    public boolean existsByClientId(String clientId) {
        var op = clientsResource.findByClientId(clientId);
        return !op.isEmpty();
    }

    @Override
    public ClientApi register(ClientApiRegister requestEntity) {
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

            return findById(id).orElseThrow();
        }
    }

    @Override
    public void updateRelacionadoId(String id, String relacionadoId) {
        var resource = this.clientsResource.get(id);

        var serviceAccountUser = resource.getServiceAccountUser();
        var userResource = this.usersResource.get(serviceAccountUser.getId());
        var userRepresentation = userResource.toRepresentation();
        var attributes = userRepresentation.getAttributes();
        attributes.put(TipoAcesso.APPLICATION_RELACIONADO_ID_KEY, Collections.singletonList(relacionadoId));

        userRepresentation.setAttributes(attributes);
        userResource.update(userRepresentation);
    }

    @Override
    public void update(String id, ClientApiUpdate requestEntity) {
        var clientResource = this.clientsResource.get(id);
        var clientRepresentation = clientResource.toRepresentation();
        clientRepresentation.setName(requestEntity.nome());
        clientResource.update(clientRepresentation);
    }

    @Override
    public void ativar(String id) {
        var clientResource = this.clientsResource.get(id);
        var client = clientResource.toRepresentation();
        client.setEnabled(Boolean.TRUE);
        clientResource.update(client);

    }

    @Override
    public void inativar(String id) {
        var clientResource = this.clientsResource.get(id);
        var client = clientResource.toRepresentation();
        client.setEnabled(Boolean.FALSE);
        clientResource.update(client);
    }

    @Override
    public void delete(String id) {
        clientsResource.get(id).remove();
    }

    @Override
    public void regenerateSecret(String id) {
        var clientResource = this.clientsResource.get(id);
        clientResource.generateNewSecret();
    }

}
