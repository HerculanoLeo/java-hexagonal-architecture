package com.herculanoleo.starter.location.estado.app.ports;

import com.herculanoleo.starter.location.estado.domain.Estado;
import com.herculanoleo.starter.location.estado.domain.EstadoSearch;
import com.herculanoleo.starter.shared.models.enums.EstadoSigla;

import java.util.Collection;
import java.util.Optional;

public interface EstadoRepositoryPort {
    Collection<Estado> findAll(EstadoSearch requestEntity);

    Optional<Estado> findById(Long id);

    Optional<Estado> findBySigla(EstadoSigla sigla);
}
