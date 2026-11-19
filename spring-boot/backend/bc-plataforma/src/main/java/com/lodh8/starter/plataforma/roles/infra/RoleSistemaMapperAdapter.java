package com.lodh8.starter.plataforma.roles.infra;

import com.lodh8.starter.identity.roles.domain.Role;
import com.lodh8.starter.plataforma.roles.app.port.RoleSistemaMapperPort;
import com.lodh8.starter.plataforma.roles.domain.RoleSistema;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleSistemaMapperAdapter extends RoleSistemaMapperPort {
    @Override
    RoleSistema domain(Role entity);
}
