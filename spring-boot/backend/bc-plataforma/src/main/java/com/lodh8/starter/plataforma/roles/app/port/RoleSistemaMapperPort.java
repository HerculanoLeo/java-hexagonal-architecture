package com.lodh8.starter.plataforma.roles.app.port;

import com.lodh8.starter.identity.roles.domain.Role;
import com.lodh8.starter.plataforma.roles.domain.RoleSistema;

public interface RoleSistemaMapperPort {
    RoleSistema domain(Role entity);
}
