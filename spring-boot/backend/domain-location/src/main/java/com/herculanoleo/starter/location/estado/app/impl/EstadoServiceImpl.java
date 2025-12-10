package com.herculanoleo.starter.location.estado.app.impl;

import com.herculanoleo.starter.location.estado.app.EstadoService;
import com.herculanoleo.starter.location.estado.app.ports.EstadoRepositoryPort;
import com.herculanoleo.starter.location.estado.domain.Estado;
import com.herculanoleo.starter.location.estado.domain.EstadoSearch;
import com.herculanoleo.starter.shared.models.enums.EstadoSigla;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EstadoServiceImpl implements EstadoService {

    private final EstadoRepositoryPort repository;

    @Override
    public Collection<Estado> findAll(EstadoSearch requestEntity) {
        return repository.findAll(requestEntity);
    }

    @Override
    public Optional<Estado> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Estado> findBySigla(EstadoSigla sigla) {
        return repository.findBySigla(sigla);
    }
}
