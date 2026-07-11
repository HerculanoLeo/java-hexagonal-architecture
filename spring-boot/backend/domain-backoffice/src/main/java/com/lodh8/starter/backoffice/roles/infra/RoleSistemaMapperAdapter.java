package com.lodh8.starter.backoffice.roles.infra;

import com.lodh8.starter.backoffice.roles.app.port.RoleSistemaMapperPort;
import com.lodh8.starter.backoffice.roles.domain.RoleSistema;
import com.lodh8.starter.identity.roles.domain.Role;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleSistemaMapperAdapter extends RoleSistemaMapperPort {
    @Override
    RoleSistema domain(Role entity);
}
