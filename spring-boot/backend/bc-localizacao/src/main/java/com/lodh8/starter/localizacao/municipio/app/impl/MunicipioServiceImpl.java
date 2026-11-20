package com.lodh8.starter.localizacao.municipio.app.impl;

import com.lodh8.starter.localizacao.estado.app.EstadoService;
import com.lodh8.starter.localizacao.municipio.app.MunicipioService;
import com.lodh8.starter.localizacao.municipio.app.port.MunicipioRepositoryPort;
import com.lodh8.starter.localizacao.municipio.domain.Municipio;
import com.lodh8.starter.localizacao.municipio.domain.MunicipioRegister;
import com.lodh8.starter.localizacao.municipio.domain.MunicipioSearch;
import com.lodh8.starter.localizacao.municipio.domain.MunicipioUpdate;
import com.lodh8.starter.shared.exceptions.NotFoundException;
import com.lodh8.starter.shared.utils.IdentificadorLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MunicipioServiceImpl implements MunicipioService {

    private final MunicipioRepositoryPort repository;

    private final EstadoService estadoService;

    @Override
    public Collection<Municipio> findAll(MunicipioSearch search) {
        log.info("municipio findAll inicio");
        log.debug("municipio findAll inicio search={}", search);
        var result = repository.findAll(search);
        log.info("municipio findAll fim total={}", result.size());
        return result;
    }

    @Override
    public Optional<Municipio> findById(Long id) {
        log.info("municipio findById id={}", id);
        var result = repository.findById(id);
        if (result.isEmpty()) {
            log.warn("municipio findById id={} resultado=nao_encontrado", id);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public Municipio register(MunicipioRegister municipio) {
        log.info("municipio register inicio estadoId={}", municipio.estadoId());
        log.debug("municipio register inicio estadoId={} nome={}", municipio.estadoId(), municipio.nome());
        estadoService.findById(municipio.estadoId()).orElseThrow(() -> {
            log.warn("municipio register estadoId={} resultado=nao_encontrado", municipio.estadoId());
            return new NotFoundException("estado não encontrado");
        });
        var created = repository.register(municipio);
        log.info("municipio register fim id={}", IdentificadorLog.mascarar(created != null ? created.id() : null));
        log.debug("municipio register fim id={}", created != null ? created.id() : null);
        return created;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void update(Long id, MunicipioUpdate municipio) {
        log.info("municipio update inicio id={}", id);
        var existing = this.repository.findById(id);
        if (existing.isEmpty()) {
            log.warn("municipio update id={} resultado=nao_encontrado", id);
        }
        existing.orElseThrow();
        var estado = estadoService.findById(municipio.estadoId());
        if (estado.isEmpty()) {
            log.warn("municipio update estadoId={} resultado=nao_encontrado", municipio.estadoId());
        }
        estado.orElseThrow();
        this.repository.update(id, municipio);
        log.info("municipio update fim id={}", id);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void delete(Long id) {
        log.info("municipio delete inicio id={}", id);
        var existing = this.repository.findById(id);
        if (existing.isEmpty()) {
            log.warn("municipio delete id={} resultado=nao_encontrado", id);
        }
        existing.orElseThrow();
        this.repository.delete(id);
        log.info("municipio delete fim id={}", id);
    }

    @Override
    public Optional<Municipio> findByNome(String nome, String uf) {
        log.info("municipio findByNome uf={}", uf);
        log.debug("municipio findByNome nome={} uf={}", nome, uf);
        var result = this.repository.findByNome(nome, uf);
        if (result.isEmpty()) {
            log.warn("municipio findByNome uf={} resultado=nao_encontrado", uf);
        }
        return result;
    }
}