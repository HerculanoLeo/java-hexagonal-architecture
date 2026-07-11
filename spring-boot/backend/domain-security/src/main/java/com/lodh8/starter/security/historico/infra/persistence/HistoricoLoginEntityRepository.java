package com.lodh8.starter.security.historico.infra.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface HistoricoLoginEntityRepository
        extends JpaRepository<HistoricoLoginEntity, UUID>, JpaSpecificationExecutor<HistoricoLoginEntity> {

    boolean existsByIdSessaoBff(String idSessaoBff);

}
