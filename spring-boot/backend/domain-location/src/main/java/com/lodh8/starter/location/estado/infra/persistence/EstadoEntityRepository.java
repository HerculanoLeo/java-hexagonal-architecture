package com.lodh8.starter.location.estado.infra.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoEntityRepository extends JpaRepository<EstadoEntity, Long>, JpaSpecificationExecutor<EstadoEntity> {
}
