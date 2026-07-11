package com.lodh8.starter.backoffice.usuarios.infra.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface UsuarioSistemaEntityRepository extends JpaRepository<UsuarioSistemaEntity, UUID>, JpaSpecificationExecutor<UsuarioSistemaEntity> {
}