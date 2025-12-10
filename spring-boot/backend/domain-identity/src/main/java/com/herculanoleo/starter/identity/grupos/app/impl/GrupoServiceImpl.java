package com.herculanoleo.starter.identity.grupos.app.impl;

import com.herculanoleo.starter.identity.grupos.app.GrupoService;
import com.herculanoleo.starter.identity.grupos.app.port.GrupoProviderPort;
import com.herculanoleo.starter.identity.grupos.domain.Grupo;
import com.herculanoleo.starter.identity.grupos.domain.GrupoRegister;
import com.herculanoleo.starter.identity.grupos.domain.GrupoSearch;
import com.herculanoleo.starter.identity.grupos.domain.GrupoUpdate;
import com.herculanoleo.starter.identity.grupos.domain.events.GrupoCriadoEvent;
import com.herculanoleo.starter.identity.grupos.domain.events.GrupoDeletadoEvent;
import com.herculanoleo.starter.shared.events.app.EventPublisherPort;
import com.herculanoleo.starter.shared.exceptions.ConflictException;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GrupoServiceImpl implements GrupoService {

    private final GrupoProviderPort grupoProvider;

    private final EventPublisherPort events;

    @Override
    public Collection<Grupo> findAll(GrupoSearch requestEntity) {
        return grupoProvider.findAll(requestEntity);
    }

    @Override
    public Optional<Grupo> findById(String id) {
        return grupoProvider.findById(id);
    }

    @Override
    public Optional<Grupo> findByIdentityId(String identityId) {
        return grupoProvider.findByIdentityId(identityId);
    }

    @Override
    public Grupo register(GrupoRegister requestEntity) {
        if (grupoProvider.existsByName(requestEntity.nome())) {
            throw new ConflictException("há um grupo com esse nome já foi cadastrado");
        }

        var grupo = grupoProvider.register(requestEntity);
        grupoProvider.updateRoles(grupo.id(), grupo.tipo(), requestEntity.roles());

        events.publishEvent(new GrupoCriadoEvent(grupo));

        return grupo;
    }

    @Override
    public void update(String id, final GrupoUpdate requestEntity) {
        var grupo = this.findById(id).orElseThrow(NotFoundException::new);

        var nomeInterno = Optional.ofNullable(grupo.relacionadoId())
                .map(relacionadoID -> "%s:ID_%s".formatted(requestEntity.nome(), relacionadoID))
                .orElseGet(requestEntity::nome);

        var nRequestEntity = requestEntity.setNomeInterno(nomeInterno);

        if (!grupo.nomeInterno().equals(nRequestEntity.nome())) {
            if (this.grupoProvider.existsByName(nRequestEntity.nome())) {
                throw new ConflictException("há um grupo com esse nome já foi cadastrado");
            }
        }

        grupoProvider.update(id, nRequestEntity);
        grupoProvider.updateRoles(id, grupo.tipo(), requestEntity.roles());
    }

    @Override
    public void delete(String id) {
        grupoProvider.delete(id);
        events.publishEvent(new GrupoDeletadoEvent(id));
    }
}
