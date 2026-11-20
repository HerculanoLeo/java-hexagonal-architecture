package com.lodh8.starter.notificacao.infra.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface NotificacaoEntityRepository extends JpaRepository<NotificacaoEntity, UUID>, JpaSpecificationExecutor<NotificacaoEntity> {
}