package com.lodh8.starter.location.estado.infra.persistence;

import com.lodh8.starter.location.estado.app.port.EstadoRepositoryPort;
import com.lodh8.starter.location.estado.domain.Estado;
import com.lodh8.starter.location.estado.domain.EstadoSearch;
import com.lodh8.starter.location.estado.infra.EstadoMapper;
import com.lodh8.starter.shared.models.enums.EstadoSigla;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

import static com.lodh8.starter.location.estado.infra.persistence.EstadoEntitySpecification.*;

@Service
@RequiredArgsConstructor
public class EstadoRepositoryAdapter implements EstadoRepositoryPort {

    private final EstadoEntityRepository repository;

    private final EstadoMapper mapper;

    @Override
    public Collection<Estado> findAll(EstadoSearch requestEntity) {
        var entities = this.repository.findAll(spec()
                .and(nome(requestEntity.nome()))
                .and(status(requestEntity.status()))
                .and(orderByNome())
        );
        return entities.stream().map(mapper::domain).toList();
    }

    @Override
    public Optional<Estado> findById(Long id) {
        return repository.findById(id).map(mapper::domain);
    }

    @Override
    public Optional<Estado> findBySigla(EstadoSigla sigla) {
        return repository.findOne(sigla(sigla)).map(mapper::domain);
    }

}
