package com.lodh8.starter.location.estado.app.impl;

import com.lodh8.starter.location.estado.app.EstadoService;
import com.lodh8.starter.location.estado.app.port.EstadoRepositoryPort;
import com.lodh8.starter.location.estado.domain.Estado;
import com.lodh8.starter.location.estado.domain.EstadoSearch;
import com.lodh8.starter.shared.models.enums.EstadoSigla;
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
