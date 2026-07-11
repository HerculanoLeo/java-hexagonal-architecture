package com.lodh8.starter.identity.roles.app;

import com.lodh8.starter.identity.roles.domain.Role;
import com.lodh8.starter.shared.models.enums.TipoAcesso;

import java.util.Collection;

public interface RoleService {
    Collection<Role> findAll();

    Collection<Role> findAllByTipo(TipoAcesso tipo);
}
