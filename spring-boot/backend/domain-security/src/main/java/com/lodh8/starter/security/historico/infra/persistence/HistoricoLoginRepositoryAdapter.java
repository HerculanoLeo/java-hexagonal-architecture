package com.lodh8.starter.security.historico.infra.persistence;

import com.lodh8.starter.security.historico.app.port.HistoricoLoginRepositoryPort;
import com.lodh8.starter.security.historico.domain.HistoricoLogin;
import com.lodh8.starter.security.historico.domain.HistoricoLoginRegister;
import com.lodh8.starter.security.historico.domain.HistoricoLoginSearch;
import com.lodh8.starter.security.historico.infra.HistoricoLoginMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class HistoricoLoginRepositoryAdapter implements HistoricoLoginRepositoryPort {

    private final HistoricoLoginEntityRepository repository;

    private final HistoricoLoginMapper mapper;

    @Override
    public Collection<HistoricoLogin> findAll(HistoricoLoginSearch search) {
        var entities = this.repository.findAll(
                HistoricoLoginEntitySpecification.spec()
                        .and(HistoricoLoginEntitySpecification.dataEventoGTEQ(search.dataEventoDe()))
                        .and(HistoricoLoginEntitySpecification.dataEventoLTEQ(search.dataEventoAte()))
                        .and(HistoricoLoginEntitySpecification.email(search.email()))
                        .and(HistoricoLoginEntitySpecification.tipo(search.tipo()))
                        .and(HistoricoLoginEntitySpecification.relacionadoId(search.relacionadoId()))
                        .and(HistoricoLoginEntitySpecification.usuarioId(search.usuarioId())),
                Sort.by(Sort.Direction.DESC, "dataEvento")
        );
        return entities.stream().map(this.mapper::domain).toList();
    }

    @Override
    public boolean existsBySessaoBffId(String sessaoBffId) {
        if (StringUtils.isBlank(sessaoBffId)) {
            return false;
        }
        return this.repository.existsByIdSessaoBff(sessaoBffId);
    }

    @Override
    public HistoricoLogin register(HistoricoLoginRegister register) {
        var entity = this.repository.save(this.mapper.entity(register));
        return this.mapper.domain(entity);
    }

}
