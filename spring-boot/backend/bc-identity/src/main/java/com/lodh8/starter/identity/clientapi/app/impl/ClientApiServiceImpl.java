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
import com.lodh8.starter.shared.utils.IdentificadorLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientApiServiceImpl implements ClientApiService {

    private final ClientApiProviderPort clientApiProvider;

    private final EventPublisherPort events;

    @Override
    public Optional<ClientApi> findById(String id) {
        log.info("clientApi findById id={}", IdentificadorLog.mascarar(id));
        log.debug("clientApi findById id={}", id);
        var result = clientApiProvider.findById(id);
        if (result.isEmpty()) {
            log.warn("clientApi findById id={} resultado=nao_encontrado", IdentificadorLog.mascarar(id));
        }
        return result;
    }

    @Override
    public ClientApi register(ClientApiRegister requestEntity) {
        log.info("clientApi register inicio clientId={}", IdentificadorLog.mascarar(requestEntity.clientId()));
        log.debug("clientApi register inicio clientId={} tipo={}", requestEntity.clientId(), requestEntity.tipo());
        if (this.clientApiProvider.existsByClientId(requestEntity.clientId())) {
            log.warn("clientApi register conflito clientId={}", IdentificadorLog.mascarar(requestEntity.clientId()));
            throw new ConflictException("client api já cadastrado com esse clientId");
        }

        var clientApi = this.clientApiProvider.register(requestEntity);

        events.publishEvent(new ClientApiCriadoEvent(clientApi));

        log.info("clientApi register fim id={}", IdentificadorLog.mascarar(clientApi.id()));
        log.debug("clientApi register fim id={} clientId={}", clientApi.id(), clientApi.clientId());
        return clientApi;
    }

    @Override
    public void update(String id, ClientApiUpdate requestEntity) {
        log.info("clientApi update inicio id={}", IdentificadorLog.mascarar(id));
        log.debug("clientApi update inicio id={}", id);
        this.clientApiProvider.update(id, requestEntity);
        log.info("clientApi update fim id={}", IdentificadorLog.mascarar(id));
    }

    @Override
    public void updateRelacionadoId(String id, String relacionadoId) {
        log.info(
                "clientApi updateRelacionadoId id={} relacionadoId={}",
                IdentificadorLog.mascarar(id),
                IdentificadorLog.mascarar(relacionadoId)
        );
        log.debug("clientApi updateRelacionadoId id={} relacionadoId={}", id, relacionadoId);
        this.clientApiProvider.updateRelacionadoId(id, relacionadoId);
        log.info("clientApi updateRelacionadoId fim id={}", IdentificadorLog.mascarar(id));
    }

    @Override
    public void ativar(String id) {
        log.info("clientApi ativar inicio id={}", IdentificadorLog.mascarar(id));
        log.debug("clientApi ativar inicio id={}", id);
        this.clientApiProvider.ativar(id);
        events.publishEvent(new ClientApiAtivadoEvent(id));
        log.info("clientApi ativar fim id={}", IdentificadorLog.mascarar(id));
    }

    @Override
    public void inativar(String id) {
        log.info("clientApi inativar inicio id={}", IdentificadorLog.mascarar(id));
        log.debug("clientApi inativar inicio id={}", id);
        this.clientApiProvider.inativar(id);
        events.publishEvent(new ClientApiInativadoEvent(id));
        log.info("clientApi inativar fim id={}", IdentificadorLog.mascarar(id));
    }

    @Override
    public void delete(String id) {
        log.info("clientApi delete inicio id={}", IdentificadorLog.mascarar(id));
        log.debug("clientApi delete inicio id={}", id);
        this.clientApiProvider.delete(id);
        log.info("clientApi delete fim id={}", IdentificadorLog.mascarar(id));
    }

    @Override
    public void regenerateSecret(String id) {
        log.info("clientApi regenerateSecret inicio id={}", IdentificadorLog.mascarar(id));
        log.debug("clientApi regenerateSecret inicio id={}", id);
        this.clientApiProvider.regenerateSecret(id);
        events.publishEvent(new ClientApiSecretRecriadaEvent(id));
        log.info("clientApi regenerateSecret fim id={}", IdentificadorLog.mascarar(id));
    }
}
