package com.lodh8.starter.identity.grupos.app.impl;

import com.lodh8.starter.identity.grupos.app.GrupoService;
import com.lodh8.starter.identity.grupos.app.port.GrupoProviderPort;
import com.lodh8.starter.identity.grupos.domain.Grupo;
import com.lodh8.starter.identity.grupos.domain.GrupoRegister;
import com.lodh8.starter.identity.grupos.domain.GrupoSearch;
import com.lodh8.starter.identity.grupos.domain.GrupoUpdate;
import com.lodh8.starter.identity.grupos.domain.events.GrupoCriadoEvent;
import com.lodh8.starter.identity.grupos.domain.events.GrupoDeletadoEvent;
import com.lodh8.starter.shared.events.app.EventPublisherPort;
import com.lodh8.starter.shared.exceptions.ConflictException;
import com.lodh8.starter.shared.utils.IdentificadorLog;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GrupoServiceImpl implements GrupoService {

    private final GrupoProviderPort grupoProvider;

    private final EventPublisherPort events;

    @Override
    public Collection<Grupo> findAll(GrupoSearch requestEntity) {
        log.info("grupo findAll inicio");
        log.debug("grupo findAll inicio search={}", requestEntity);
        var result = grupoProvider.findAll(requestEntity);
        log.info("grupo findAll fim total={}", result.size());
        return result;
    }

    @Override
    public Optional<Grupo> findById(String id) {
        log.info("grupo findById id={}", IdentificadorLog.mascarar(id));
        log.debug("grupo findById id={}", id);
        var result = grupoProvider.findById(id);
        if (result.isEmpty()) {
            log.warn("grupo findById id={} resultado=nao_encontrado", IdentificadorLog.mascarar(id));
        }
        return result;
    }

    @Override
    public Optional<Grupo> findByIdentityId(String identityId) {
        log.info("grupo findByIdentityId identityId={}", IdentificadorLog.mascarar(identityId));
        log.debug("grupo findByIdentityId identityId={}", identityId);
        var result = grupoProvider.findByIdentityId(identityId);
        log.info(
                "grupo findByIdentityId identityId={} resultado={}",
                IdentificadorLog.mascarar(identityId),
                result.isPresent() ? "ok" : "nao_encontrado"
        );
        log.debug(
                "grupo findByIdentityId identityId={} resultado={}",
                identityId,
                result.isPresent() ? "ok" : "nao_encontrado"
        );
        return result;
    }

    @Override
    public Grupo register(GrupoRegister requestEntity) {
        log.info("grupo register inicio nome={}", IdentificadorLog.mascarar(requestEntity.nome()));
        log.debug("grupo register inicio nome={} tipo={}", requestEntity.nome(), requestEntity.tipo());
        if (grupoProvider.existsByName(requestEntity.nome())) {
            log.warn("grupo register conflito nome={}", IdentificadorLog.mascarar(requestEntity.nome()));
            throw new ConflictException("há um grupo com esse nome já foi cadastrado");
        }

        var grupo = grupoProvider.register(requestEntity);
        grupoProvider.updateRoles(grupo.id(), grupo.tipo(), requestEntity.roles());

        events.publishEvent(new GrupoCriadoEvent(grupo));

        log.info("grupo register fim id={}", IdentificadorLog.mascarar(grupo.id()));
        log.debug("grupo register fim id={} nome={}", grupo.id(), grupo.nome());
        return grupo;
    }

    @Override
    public void update(String id, final GrupoUpdate requestEntity) {
        log.info("grupo update inicio id={}", IdentificadorLog.mascarar(id));
        log.debug("grupo update inicio id={} nome={}", id, requestEntity.nome());
        var grupo = this.findById(id).orElseThrow(NotFoundException::new);

        var nomeInterno = Optional.ofNullable(grupo.relacionadoId())
                .map(relacionadoID -> "%s:ID_%s".formatted(requestEntity.nome(), relacionadoID))
                .orElseGet(requestEntity::nome);

        var nRequestEntity = requestEntity.setNomeInterno(nomeInterno);

        if (!grupo.nomeInterno().equals(nRequestEntity.nome())) {
            if (this.grupoProvider.existsByName(nRequestEntity.nome())) {
                log.warn("grupo update conflito id={} nome={}", IdentificadorLog.mascarar(id), IdentificadorLog.mascarar(nRequestEntity.nome()));
                throw new ConflictException("há um grupo com esse nome já foi cadastrado");
            }
        }

        grupoProvider.update(id, nRequestEntity);
        grupoProvider.updateRoles(id, grupo.tipo(), requestEntity.roles());
        log.info("grupo update fim id={}", IdentificadorLog.mascarar(id));
        log.debug("grupo update fim id={}", id);
    }

    @Override
    public void delete(String id) {
        log.info("grupo delete inicio id={}", IdentificadorLog.mascarar(id));
        log.debug("grupo delete inicio id={}", id);
        grupoProvider.delete(id);
        events.publishEvent(new GrupoDeletadoEvent(id));
        log.info("grupo delete fim id={}", IdentificadorLog.mascarar(id));
        log.debug("grupo delete fim id={}", id);
    }

    @Override
    public boolean hasMembers(String id) {
        log.info("grupo hasMembers id={}", IdentificadorLog.mascarar(id));
        log.debug("grupo hasMembers id={}", id);
        var has = grupoProvider.hasMembers(id);
        log.info("grupo hasMembers id={} resultado={}", IdentificadorLog.mascarar(id), has);
        log.debug("grupo hasMembers id={} resultado={}", id, has);
        return has;
    }
}
