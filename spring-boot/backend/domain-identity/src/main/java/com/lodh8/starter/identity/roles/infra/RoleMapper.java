package com.lodh8.starter.identity.roles.infra;

import com.herculanoleo.spring.me.models.enums.MapperEnum;
import com.lodh8.starter.identity.roles.domain.Role;
import com.lodh8.starter.shared.models.enums.TipoAcesso;
import org.keycloak.representations.idm.RoleRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.util.List;
import java.util.Map;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "nome")
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "composites", ignore = true)
    @Mapping(target = "composite", ignore = true)
    @Mapping(target = "clientRole", ignore = true)
    @Mapping(target = "containerId", ignore = true)
    @Mapping(target = "attributes", ignore = true)
    RoleRepresentation representation(Role role);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "name")
    @Mapping(target = "descricao", source = "description")
    @Mapping(source = "attributes", target = "tipo", qualifiedByName = "mapAttributesToTipo")
    Role dto(RoleRepresentation representation);

    @Named("mapAttributesToTipo")
    default TipoAcesso mapAttributesToTipo(Map<String, List<String>> attributes) {
        var values = attributes.get(TipoAcesso.APPLICATION_TYPE_KEY);
        if (values == null || values.isEmpty()) {
            return null;
        }
        return MapperEnum.fromValue(values.getFirst(), TipoAcesso.class);
    }

}
