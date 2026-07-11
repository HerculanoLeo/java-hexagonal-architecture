package com.lodh8.starter.identity.clientapi.app.impl;

import com.lodh8.starter.identity.clientapi.app.ClientApiService;
import com.lodh8.starter.identity.clientapi.app.port.ClientApiProviderPort;
import com.lodh8.starter.identity.clientapi.domain.ClientApi;
import com.lodh8.starter.identity.clientapi.domain.ClientApiRegister;
import com.lodh8.starter.identity.clientapi.domain.ClientApiUpdate;
import com.lodh8.starter.identity.clientapi.domain.events.ClientApiAtivadoEvent;
import com.lodh8.starter.identity.clientapi.domain.events.ClientApiCriadoEvent;
import com.lodh8.starter.identity.clientapi.domain.events.ClientApiInativadoEvent;
import com.lodh8.starter.identity.clientapi.domain.events.ClientApiSecretRecriadaEvent;
import com.lodh8.starter.shared.events.app.EventPublisherPort;
import com.lodh8.starter.shared.exceptions.ConflictException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientApiServiceImpl implements ClientApiService {

    private final ClientApiProviderPort clientApiProvider;

    private final EventPublisherPort events;

    @Override
    public Optional<ClientApi> findById(String id) {
        return clientApiProvider.findById(id);
    }

    @Override
    public ClientApi register(ClientApiRegister requestEntity) {
        if (this.clientApiProvider.existsByClientId(requestEntity.clientId())) {
            throw new ConflictException("client api já cadastrado com esse clientId");
        }

        var clientApi = this.clientApiProvider.register(requestEntity);

        events.publishEvent(new ClientApiCriadoEvent(clientApi));

        return clientApi;
    }

    @Override
    public void update(String id, ClientApiUpdate requestEntity) {
        this.clientApiProvider.update(id, requestEntity);
    }

    @Override
    public void updateRelacionadoId(String id, String relacionadoId) {
        this.clientApiProvider.updateRelacionadoId(id, relacionadoId);
    }

    @Override
    public void ativar(String id) {
        this.clientApiProvider.ativar(id);
        events.publishEvent(new ClientApiAtivadoEvent(id));
    }

    @Override
    public void inativar(String id) {
        this.clientApiProvider.inativar(id);
        events.publishEvent(new ClientApiInativadoEvent(id));
    }

    @Override
    public void delete(String id) {
        this.clientApiProvider.delete(id);
    }

    @Override
    public void regenerateSecret(String id) {
        this.clientApiProvider.regenerateSecret(id);
        events.publishEvent(new ClientApiSecretRecriadaEvent(id));
    }
}
