package com.herculanoleo.starter.plataformadmin.roles.infra;

import com.herculanoleo.starter.identity.roles.domain.Role;
import com.herculanoleo.starter.plataformadmin.roles.app.port.RoleSistemaMapperPort;
import com.herculanoleo.starter.plataformadmin.roles.domain.RoleSistema;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleSistemaMapperAdapter extends RoleSistemaMapperPort {
    @Override
    RoleSistema domain(Role entity);
}
