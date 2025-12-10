package com.herculanoleo.starter.plataformadmin.roles.infra;

import com.herculanoleo.starter.plataformadmin.roles.api.dtos.RoleSistemaDTO;
import com.herculanoleo.starter.plataformadmin.roles.domain.RoleSistema;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleSistemaDTOMapper {
    RoleSistemaDTO dto(RoleSistema entity);
}
