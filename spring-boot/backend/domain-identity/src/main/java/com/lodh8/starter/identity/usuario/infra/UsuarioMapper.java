package com.lodh8.starter.identity.usuario.infra;

import com.herculanoleo.spring.me.models.enums.MapperEnum;
import com.lodh8.starter.identity.usuario.domain.Usuario;
import com.lodh8.starter.identity.usuario.domain.UsuarioRegister;
import com.lodh8.starter.shared.models.enums.TipoAcesso;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UsuarioMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "nome", target = "firstName")
    @Mapping(source = "senha", target = "credentials", qualifiedByName = "mapPasswordToCredentials")
    @Mapping(target = "attributes", ignore = true)
    @Mapping(target = "groups", ignore = true)
    @Mapping(target = "lastName", ignore = true)
    @Mapping(target = "username", source = "email")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "userProfileMetadata", ignore = true)
    @Mapping(target = "self", ignore = true)
    @Mapping(target = "createdTimestamp", ignore = true)
    @Mapping(target = "totp", ignore = true)
    @Mapping(target = "requiredActions", ignore = true)
    @Mapping(target = "federatedIdentities", ignore = true)
    @Mapping(target = "socialLinks", ignore = true)
    @Mapping(target = "realmRoles", ignore = true)
    @Mapping(target = "clientRoles", ignore = true)
    @Mapping(target = "clientConsents", ignore = true)
    @Mapping(target = "notBefore", ignore = true)
    @Mapping(target = "federationLink", ignore = true)
    @Mapping(target = "serviceAccountClientId", ignore = true)
    @Mapping(target = "origin", ignore = true)
    @Mapping(target = "disableableCredentialTypes", ignore = true)
    @Mapping(target = "access", ignore = true)
    @Mapping(target = "rawAttributes", ignore = true)
    @Mapping(target = "applicationRoles", ignore = true)
    UserRepresentation representation(UsuarioRegister requestEntity);

    @Named("mapPasswordToCredentials")
    default List<CredentialRepresentation> mapPasswordToCredentials(String password) {
        if (password == null || password.isBlank()) {
            return Collections.emptyList();
        }

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);

        return Collections.singletonList(credential);
    }

    @AfterMapping
    default void mapCustomAttributes(@MappingTarget UserRepresentation representation, UsuarioRegister dto) {
        representation.setAttributes(dto.attributes());
    }

    @Mapping(source = "id", target = "id")
    @Mapping(source = "firstName", target = "nome")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "emailVerified", target = "emailVerified")
    @Mapping(source = "enabled", target = "enabled")
    @Mapping(source = "attributes", target = "relacionadoId", qualifiedByName = "mapAttributesToRelacionadoId")
    @Mapping(source = "attributes", target = "tipo", qualifiedByName = "mapAttributesToTipo")
    Usuario dto(UserRepresentation representation);

    @Named("mapAttributesToRelacionadoId")
    default String mapAttributesToRelacionadoId(Map<String, List<String>> attributes) {
        var values = attributes.get(TipoAcesso.APPLICATION_RELACIONADO_ID_KEY);
        if (values == null || values.isEmpty()) {
            return null;
        }
        return values.getFirst();
    }

    @Named("mapAttributesToTipo")
    default TipoAcesso mapAttributesToTipo(Map<String, List<String>> attributes) {
        var values = attributes.get(TipoAcesso.APPLICATION_TYPE_KEY);

        if (values == null || values.isEmpty()) {
            return TipoAcesso.ANONYMOUS;
        }

        return MapperEnum.fromValue(values.getFirst(), TipoAcesso.class);
    }

}
