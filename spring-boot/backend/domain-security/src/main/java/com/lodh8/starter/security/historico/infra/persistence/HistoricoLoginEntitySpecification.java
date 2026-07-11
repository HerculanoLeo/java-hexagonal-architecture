package com.lodh8.starter.security.historico.infra.persistence;

import com.lodh8.starter.shared.models.enums.TipoAcesso;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.time.OffsetDateTime;
import java.util.UUID;

public final class HistoricoLoginEntitySpecification {

    private HistoricoLoginEntitySpecification() {
    }

    public static Specification<HistoricoLoginEntity> spec() {
        return Specification.unrestricted();
    }

    public static Specification<HistoricoLoginEntity> dataEventoGTEQ(OffsetDateTime dataEvento) {
        return (root, _, criteriaBuilder) -> {
            if (dataEvento != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get(HistoricoLoginEntity_.dataEvento), dataEvento);
            }
            return null;
        };
    }

    public static Specification<HistoricoLoginEntity> dataEventoLTEQ(OffsetDateTime dataEvento) {
        return (root, _, criteriaBuilder) -> {
            if (dataEvento != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get(HistoricoLoginEntity_.dataEvento), dataEvento);
            }
            return null;
        };
    }

    public static Specification<HistoricoLoginEntity> email(String email) {
        return (root, _, criteriaBuilder) -> {
            if (StringUtils.isNotBlank(email)) {
                return criteriaBuilder.like(
                        criteriaBuilder.lower(root.get(HistoricoLoginEntity_.email)),
                        "%" + email.trim().toLowerCase() + "%"
                );
            }
            return null;
        };
    }

    public static Specification<HistoricoLoginEntity> tipo(TipoAcesso tipo) {
        return (root, _, criteriaBuilder) -> {
            if (tipo != null) {
                return criteriaBuilder.equal(root.get(HistoricoLoginEntity_.tipo), tipo);
            }
            return null;
        };
    }

    public static Specification<HistoricoLoginEntity> relacionadoId(String relacionadoId) {
        return (root, _, criteriaBuilder) -> {
            if (StringUtils.isNotBlank(relacionadoId)) {
                return criteriaBuilder.equal(root.get(HistoricoLoginEntity_.idRelacionado), relacionadoId.trim());
            }
            return null;
        };
    }

    public static Specification<HistoricoLoginEntity> usuarioId(UUID usuarioId) {
        return (root, _, criteriaBuilder) -> {
            if (usuarioId != null) {
                return criteriaBuilder.equal(root.get(HistoricoLoginEntity_.idUsuario), usuarioId);
            }
            return null;
        };
    }

}
