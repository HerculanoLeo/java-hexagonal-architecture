package com.herculanoleo.starter.plataformadmin.usuarios.infra.persistence;

import com.herculanoleo.starter.shared.models.enums.Status;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class UsuarioSistemaEntitySpecification {

    public static Specification<UsuarioSistemaEntity> spec() {
        return Specification.unrestricted();
    }

    public static Specification<UsuarioSistemaEntity> likeNome(String nome) {
        return (root, query, builder) -> {
            if (StringUtils.isNotBlank(nome)) {
                return builder.like(
                        builder.upper(root.get(UsuarioSistemaEntity_.nome)), "%%%s%%".formatted(nome).toUpperCase()
                );
            }
            return null;
        };
    }

    public static Specification<UsuarioSistemaEntity> likeEmail(String email) {
        return (root, query, builder) -> {
            if (StringUtils.isNotBlank(email)) {
                return builder.like(
                        builder.upper(root.get(UsuarioSistemaEntity_.email)), "%%%s%%".formatted(email).toUpperCase()
                );
            }
            return null;
        };
    }

    public static Specification<UsuarioSistemaEntity> email(String email) {
        return (root, query, criteriaBuilder) -> {
            if (StringUtils.isNotBlank(email)) {
                return criteriaBuilder.equal(root.get(UsuarioSistemaEntity_.email), email.toLowerCase());
            }
            return null;
        };
    }

    public static Specification<UsuarioSistemaEntity> identityId(String identityId) {
        return (root, query, criteriaBuilder) -> {
            if (StringUtils.isNotBlank(identityId)) {
                return criteriaBuilder.equal(root.get(UsuarioSistemaEntity_.identityId), identityId.toLowerCase());
            }
            return null;
        };
    }

    public static Specification<UsuarioSistemaEntity> status(Status status) {
        return (root, query, builder) -> {
            if (status != null) {
                return builder.equal(root.get(UsuarioSistemaEntity_.status), status);
            }
            return null;
        };
    }
}
