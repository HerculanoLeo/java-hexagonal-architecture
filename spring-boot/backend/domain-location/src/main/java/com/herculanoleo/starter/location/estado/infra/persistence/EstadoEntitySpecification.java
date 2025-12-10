package com.herculanoleo.starter.location.estado.infra.persistence;

import com.herculanoleo.starter.shared.models.enums.EstadoSigla;
import com.herculanoleo.starter.shared.models.enums.Status;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public final class EstadoEntitySpecification {

    private EstadoEntitySpecification() {
    }

    public static Specification<EstadoEntity> spec() {
        return Specification.unrestricted();
    }

    public static Specification<EstadoEntity> nome(String nome) {
        return (root, _, criteriaBuilder) -> {
            if (StringUtils.hasText(nome)) {
                return criteriaBuilder.like(criteriaBuilder.lower(root.get(EstadoEntity_.nome)), "%" + nome.toLowerCase() + "%");
            }
            return null;
        };
    }

    public static Specification<EstadoEntity> sigla(EstadoSigla sigla) {
        return (root, _, criteriaBuilder) -> {
            if (sigla != null) {
                return criteriaBuilder.equal(root.get(EstadoEntity_.sigla), sigla);
            }
            return null;
        };
    }

    public static Specification<EstadoEntity> status(Status status) {
        return (root, _, criteriaBuilder) -> {
            if (status != null) {
                return criteriaBuilder.equal(root.get(EstadoEntity_.status), status);
            }
            return null;
        };
    }

    public static Specification<EstadoEntity> orderByNome() {
        return (root, query, criteriaBuilder) -> {
            if (null != query) {
                query.orderBy(criteriaBuilder.asc(root.get(EstadoEntity_.nome)));
            }
            return null;
        };
    }

}
