package com.herculanoleo.starter.identity.roles.app.port;

import com.herculanoleo.starter.identity.roles.domain.Role;
import com.herculanoleo.starter.shared.models.enums.TipoAcesso;

import java.util.Collection;

public interface RoleProviderPort {
    Collection<Role> findAll();

    Collection<Role> findAllByTipo(TipoAcesso tipo);
}
