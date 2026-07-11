package com.lodh8.starter.location.municipio.infra.persistence;

import com.lodh8.starter.location.estado.infra.persistence.EstadoEntity_;
import com.lodh8.starter.shared.models.enums.EstadoSigla;
import com.lodh8.starter.shared.models.enums.Status;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public final class MunicipioEntitySpecification {

    private MunicipioEntitySpecification() {
    }

    public static Specification<MunicipioEntity> spec() {
        return Specification.unrestricted();
    }

    public static Specification<MunicipioEntity> nome(String nome) {
        return (root, _, criteriaBuilder) -> {
            if (StringUtils.hasText(nome)) {
                return criteriaBuilder.like(criteriaBuilder.lower(root.get(MunicipioEntity_.nome)), "%" + nome.toLowerCase() + "%");
            }
            return null;
        };
    }

    public static Specification<MunicipioEntity> estadoId(Long estadoId) {
        return (root, _, criteriaBuilder) -> {
            if (estadoId != null) {
                return criteriaBuilder.equal(root.get(MunicipioEntity_.estadoId), estadoId);
            }
            return null;
        };
    }

    public static Specification<MunicipioEntity> sigla(EstadoSigla sigla) {
        return (root, _, criteriaBuilder) -> {
            if (sigla != null) {
                return criteriaBuilder.equal(root.get(MunicipioEntity_.estado).get(EstadoEntity_.sigla), sigla);
            }
            return null;
        };
    }

    public static Specification<MunicipioEntity> status(Status status) {
        return (root, _, criteriaBuilder) -> {
            if (status != null) {
                return criteriaBuilder.equal(root.get(MunicipioEntity_.status), status);
            }
            return null;
        };
    }

    public static Specification<MunicipioEntity> fetchEstado(boolean fetch) {
        return (root, _, _) -> {
            if (fetch) {
                root.fetch(MunicipioEntity_.estado);
            }
            return null;
        };
    }

    public static Specification<MunicipioEntity> orderByNome() {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.asc(root.get(MunicipioEntity_.nome)));
            return null;
        };
    }
}
