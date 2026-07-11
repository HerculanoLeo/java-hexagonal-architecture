package com.lodh8.starter.backoffice.roles.infra;

import com.lodh8.starter.backoffice.roles.api.dtos.RoleSistemaDTO;
import com.lodh8.starter.backoffice.roles.domain.RoleSistema;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleSistemaDTOMapper {
    RoleSistemaDTO dto(RoleSistema entity);
}
