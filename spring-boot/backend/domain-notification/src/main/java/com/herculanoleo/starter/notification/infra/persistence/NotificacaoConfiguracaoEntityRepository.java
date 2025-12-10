package com.herculanoleo.starter.notification.infra.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface NotificacaoConfiguracaoEntityRepository extends JpaRepository<NotificacaoConfiguracaoEntity, UUID>, JpaSpecificationExecutor<NotificacaoConfiguracaoEntity> {
}