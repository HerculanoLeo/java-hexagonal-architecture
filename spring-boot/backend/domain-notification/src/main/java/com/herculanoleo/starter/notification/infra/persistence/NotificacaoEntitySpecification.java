package com.herculanoleo.starter.notification.infra.persistence;

import com.herculanoleo.starter.shared.models.enums.NotificacaoStatus;
import com.herculanoleo.starter.shared.models.enums.TipoNotificacao;
import org.springframework.data.jpa.domain.Specification;

import java.time.OffsetDateTime;

public final class NotificacaoEntitySpecification {

    private NotificacaoEntitySpecification() {
    }

    public static Specification<NotificacaoEntity> spec() {
        return Specification.unrestricted();
    }

    public static Specification<NotificacaoEntity> status(NotificacaoStatus status) {
        return (root, _, criteriaBuilder) -> {
            if (status != null) {
                return criteriaBuilder.equal(root.get(NotificacaoEntity_.status), status);
            }
            return null;
        };
    }

    public static Specification<NotificacaoEntity> tipo(TipoNotificacao tipo) {
        return (root, _, criteriaBuilder) -> {
            if (tipo != null) {
                return criteriaBuilder.equal(root.get(NotificacaoEntity_.tipo), tipo);
            }
            return null;
        };
    }

    public static Specification<NotificacaoEntity> tentativasGTEQ(Integer tentativas) {
        return (root, _, criteriaBuilder) -> {
            if (tentativas != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get(NotificacaoEntity_.tentativas), tentativas);
            }
            return null;
        };
    }

    public static Specification<NotificacaoEntity> tentativasLTEQ(Integer tentativas) {
        return (root, _, criteriaBuilder) -> {
            if (tentativas != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get(NotificacaoEntity_.tentativas), tentativas);
            }
            return null;
        };
    }

    public static Specification<NotificacaoEntity> dataSolicitacaoGTEQ(OffsetDateTime dataSolicitacao) {
        return (root, _, criteriaBuilder) -> {
            if (dataSolicitacao != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get(NotificacaoEntity_.dataSolicitacao), dataSolicitacao);
            }
            return null;
        };
    }

    public static Specification<NotificacaoEntity> dataSolicitacaoLTEQ(OffsetDateTime dataSolicitacao) {
        return (root, _, criteriaBuilder) -> {
            if (dataSolicitacao != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get(NotificacaoEntity_.dataSolicitacao), dataSolicitacao);
            }
            return null;
        };
    }

}
