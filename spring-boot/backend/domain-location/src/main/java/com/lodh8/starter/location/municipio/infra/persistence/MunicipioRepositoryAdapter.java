package com.lodh8.starter.location.municipio.infra.persistence;

import com.lodh8.starter.location.municipio.app.port.MunicipioRepositoryPort;
import com.lodh8.starter.location.municipio.domain.Municipio;
import com.lodh8.starter.location.municipio.domain.MunicipioRegister;
import com.lodh8.starter.location.municipio.domain.MunicipioSearch;
import com.lodh8.starter.location.municipio.domain.MunicipioUpdate;
import com.lodh8.starter.location.municipio.infra.MunicipioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

import static com.lodh8.starter.location.municipio.infra.persistence.MunicipioEntitySpecification.*;

@Service
@RequiredArgsConstructor
public class MunicipioRepositoryAdapter implements MunicipioRepositoryPort {

    private final MunicipioEntityRepository repository;

    private final MunicipioMapper mapper;

    @Override
    public Collection<Municipio> findAll(MunicipioSearch requestEntity) {
        var entities = repository.findAll(spec()
                .and(nome(requestEntity.nome()))
                .and(status(requestEntity.status()))
                .and(estadoId(requestEntity.estadoId()))
                .and(sigla(requestEntity.sigla()))
                .and(fetchEstado(true))
                .and(orderByNome())
        );
        return entities.stream().map(mapper::domain).toList();
    }

    @Override
    public Optional<Municipio> findById(Long id) {
        return repository.findById(id).map(mapper::domain);
    }

    @Override
    public Municipio register(MunicipioRegister requestEntity) {
        var entity = repository.save(mapper.entity(requestEntity));
        return mapper.domain(entity);
    }

    @Override
    public void update(Long id, MunicipioUpdate requestEntity) {
        var entity = repository.findById(id).orElseThrow();

        entity.setNome(requestEntity.nome());
        entity.setStatus(requestEntity.status());
        entity.setEstadoId(requestEntity.estadoId());

        repository.save(entity);
    }

    @Override
    public void delete(Long id) {
        this.repository.deleteById(id);
    }

    @Override
    public Optional<Municipio> findByNome(String nome, String uf) {
        return this.repository.findByNome(nome, uf).map(mapper::domain);
    }

}
