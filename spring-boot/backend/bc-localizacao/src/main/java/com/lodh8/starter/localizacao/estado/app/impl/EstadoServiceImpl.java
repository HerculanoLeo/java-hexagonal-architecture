package com.lodh8.starter.localizacao.estado.app.impl;

import com.lodh8.starter.localizacao.estado.app.EstadoService;
import com.lodh8.starter.localizacao.estado.app.port.EstadoRepositoryPort;
import com.lodh8.starter.localizacao.estado.domain.Estado;
import com.lodh8.starter.localizacao.estado.domain.EstadoSearch;
import com.lodh8.starter.shared.models.enums.EstadoSigla;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EstadoServiceImpl implements EstadoService {

    private final EstadoRepositoryPort repository;

    @Override
    public Collection<Estado> findAll(EstadoSearch requestEntity) {
        log.info("estado findAll inicio");
        log.debug("estado findAll inicio search={}", requestEntity);
        var result = repository.findAll(requestEntity);
        log.info("estado findAll fim total={}", result.size());
        return result;
    }

    @Override
    public Optional<Estado> findById(Long id) {
        log.info("estado findById id={}", id);
        var result = repository.findById(id);
        if (result.isEmpty()) {
            log.warn("estado findById id={} resultado=nao_encontrado", id);
        }
        return result;
    }

    @Override
    public Optional<Estado> findBySigla(EstadoSigla sigla) {
        log.info("estado findBySigla sigla={}", sigla);
        var result = repository.findBySigla(sigla);
        if (result.isEmpty()) {
            log.warn("estado findBySigla sigla={} resultado=nao_encontrado", sigla);
        }
        return result;
    }
}