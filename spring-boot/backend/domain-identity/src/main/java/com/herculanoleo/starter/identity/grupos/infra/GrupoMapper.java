package com.herculanoleo.starter.identity.grupos.infra;

import com.herculanoleo.spring.me.models.enums.MapperEnum;
import com.herculanoleo.starter.identity.grupos.domain.Grupo;
import com.herculanoleo.starter.identity.grupos.domain.GrupoRegister;
import com.herculanoleo.starter.shared.models.enums.TipoAcesso;
import org.keycloak.representations.idm.GroupRepresentation;
import org.mapstruct.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface GrupoMapper {

    @Mapping(target = "name", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "path", ignore = true)
    @Mapping(target = "parentId", ignore = true)
    @Mapping(target = "subGroupCount", ignore = true)
    @Mapping(target = "realmRoles", ignore = true)
    @Mapping(target = "clientRoles", ignore = true)
    @Mapping(target = "attributes", ignore = true)
    @Mapping(target = "subGroups", ignore = true)
    @Mapping(target = "access", ignore = true)
    GroupRepresentation representation(GrupoRegister requestEntity);

    @AfterMapping
    default void mapCustomAttributes(@MappingTarget GroupRepresentation representation, GrupoRegister dto) {
        representation.setAttributes(dto.attributes());
        representation.setName(dto.nomeInterno());
    }

    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = ".", qualifiedByName = "mapNomeInternoToNome")
    @Mapping(target = "roles", source = "realmRoles")
    @Mapping(source = "attributes", target = "relacionadoId", qualifiedByName = "mapAttributesToRelacionadoId")
    @Mapping(source = "attributes", target = "tipo", qualifiedByName = "mapAttributesToTipo")
    Grupo dto(GroupRepresentation representation);

    @Named("mapNomeInternoToNome")
    default String mapNomeInternoToNome(GroupRepresentation representation) {
        return Arrays.asList(representation.getName().split(":")).getFirst();
    }

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
            return null;
        }

        return MapperEnum.fromValue(values.getFirst(), TipoAcesso.class);
    }

}
