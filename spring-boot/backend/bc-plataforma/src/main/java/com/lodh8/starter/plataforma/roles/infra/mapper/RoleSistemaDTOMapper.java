package com.lodh8.starter.plataforma.roles.infra.mapper;

import com.lodh8.starter.plataforma.roles.api.dtos.RoleSistemaDTO;
import com.lodh8.starter.plataforma.roles.domain.RoleSistema;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleSistemaDTOMapper {
    RoleSistemaDTO dto(RoleSistema entity);
}
