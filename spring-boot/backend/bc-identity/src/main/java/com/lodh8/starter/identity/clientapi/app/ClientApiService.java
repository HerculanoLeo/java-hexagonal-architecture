package com.lodh8.starter.identity.clientapi.app;

import com.lodh8.starter.identity.clientapi.domain.ClientApi;
import com.lodh8.starter.identity.clientapi.domain.ClientApiRegister;
import com.lodh8.starter.identity.clientapi.domain.ClientApiUpdate;

import java.util.Optional;

public interface ClientApiService {
    Optional<ClientApi> findById(String id);

    ClientApi register(ClientApiRegister requestEntity);

    void update(String id, ClientApiUpdate requestEntity);

    void updateRelacionadoId(String id, String relacionadoId);

    void ativar(String id);

    void inativar(String id);

    void delete(String id);

    void regenerateSecret(String id);
}
