package com.lodh8.starter.backoffice.roles.app.port;

import com.lodh8.starter.backoffice.roles.domain.RoleSistema;
import com.lodh8.starter.identity.roles.domain.Role;

public interface RoleSistemaMapperPort {
    RoleSistema domain(Role entity);
}
