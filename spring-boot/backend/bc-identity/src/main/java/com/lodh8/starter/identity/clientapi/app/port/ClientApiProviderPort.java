package com.lodh8.starter.identity.clientapi.app.port;

import com.lodh8.starter.identity.clientapi.domain.ClientApi;
import com.lodh8.starter.identity.clientapi.domain.ClientApiRegister;
import com.lodh8.starter.identity.clientapi.domain.ClientApiUpdate;

import java.util.Optional;

public interface ClientApiProviderPort {
    Optional<ClientApi> findById(String id);

    boolean existsByClientId(String clientId);

    ClientApi register(ClientApiRegister requestEntity);

    void updateRelacionadoId(String id, String relacionadoId);

    void update(String id, ClientApiUpdate requestEntity);

    void ativar(String id);

    void inativar(String id);

    void delete(String id);

    void regenerateSecret(String id);
}
