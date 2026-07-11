package com.lodh8.starter.location.estado.app;

import com.lodh8.starter.location.estado.domain.Estado;
import com.lodh8.starter.location.estado.domain.EstadoSearch;
import com.lodh8.starter.shared.models.enums.EstadoSigla;

import java.util.Collection;
import java.util.Optional;

public interface EstadoService {
    Collection<Estado> findAll(EstadoSearch requestEntity);

    Optional<Estado> findById(Long id);

    Optional<Estado> findBySigla(EstadoSigla sigla);
}
