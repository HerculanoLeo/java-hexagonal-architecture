package com.lodh8.starter.localizacao.estado.app.port;

import com.lodh8.starter.localizacao.estado.domain.Estado;
import com.lodh8.starter.localizacao.estado.domain.EstadoSearch;
import com.lodh8.starter.shared.models.enums.EstadoSigla;

import java.util.Collection;
import java.util.Optional;

public interface EstadoRepositoryPort {
    Collection<Estado> findAll(EstadoSearch requestEntity);

    Optional<Estado> findById(Long id);

    Optional<Estado> findBySigla(EstadoSigla sigla);
}
