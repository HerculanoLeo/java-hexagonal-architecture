package com.lodh8.starter.notification.infra.persistence;

import com.lodh8.starter.shared.models.enums.NotificacaoConfiguracaoCodigo;
import org.springframework.data.jpa.domain.Specification;

public class NotificacaoConfiguracaoEntitySpecification {

    private NotificacaoConfiguracaoEntitySpecification() {
    }

    public static Specification<NotificacaoConfiguracaoEntity> spec() {
        return Specification.unrestricted();
    }

    public static Specification<NotificacaoConfiguracaoEntity> codigo(NotificacaoConfiguracaoCodigo codigo) {
        return (root, query, criteriaBuilder) -> {
            if (null != codigo) {
                return criteriaBuilder.equal(root.get(NotificacaoConfiguracaoEntity_.codigo), codigo);
            }
            return null;
        };
    }


}
