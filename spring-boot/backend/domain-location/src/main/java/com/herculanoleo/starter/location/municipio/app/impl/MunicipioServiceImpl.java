package com.herculanoleo.starter.location.municipio.app.impl;

import com.herculanoleo.starter.location.estado.app.EstadoService;
import com.herculanoleo.starter.location.municipio.app.MunicipioService;
import com.herculanoleo.starter.location.municipio.app.ports.MunicipioRepositoryPort;
import com.herculanoleo.starter.location.municipio.domain.Municipio;
import com.herculanoleo.starter.location.municipio.domain.MunicipioRegister;
import com.herculanoleo.starter.location.municipio.domain.MunicipioSearch;
import com.herculanoleo.starter.location.municipio.domain.MunicipioUpdate;
import com.herculanoleo.starter.shared.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MunicipioServiceImpl implements MunicipioService {

    private final MunicipioRepositoryPort repository;

    private final EstadoService estadoService;

    @Override
    public Collection<Municipio> findAll(MunicipioSearch search) {
        return repository.findAll(search);
    }

    @Override
    public Optional<Municipio> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public Municipio register(MunicipioRegister municipio) {
        estadoService.findById(municipio.estadoId()).orElseThrow(() -> new NotFoundException("estado não encontrado"));
        return repository.register(municipio);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void update(Long id, MunicipioUpdate municipio) {
        this.repository.findById(id).orElseThrow();
        estadoService.findById(municipio.estadoId()).orElseThrow();
        this.repository.update(id, municipio);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void delete(Long id) {
        this.repository.findById(id).orElseThrow();
        this.repository.delete(id);
    }

    @Override
    public Optional<Municipio> findByNome(String nome, String uf) {
        return this.repository.findByNome(nome, uf);
    }
}
