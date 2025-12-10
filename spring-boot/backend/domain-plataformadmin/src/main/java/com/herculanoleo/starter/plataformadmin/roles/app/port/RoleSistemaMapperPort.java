package com.herculanoleo.starter.plataformadmin.roles.app.port;

import com.herculanoleo.starter.identity.roles.domain.Role;
import com.herculanoleo.starter.plataformadmin.roles.domain.RoleSistema;

public interface RoleSistemaMapperPort {
    RoleSistema domain(Role entity);
}
