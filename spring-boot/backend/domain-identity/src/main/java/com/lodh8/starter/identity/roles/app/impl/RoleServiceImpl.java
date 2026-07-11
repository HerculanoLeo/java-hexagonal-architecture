package com.lodh8.starter.identity.roles.app.impl;

import com.lodh8.starter.identity.roles.app.RoleService;
import com.lodh8.starter.identity.roles.app.port.RoleProviderPort;
import com.lodh8.starter.identity.roles.domain.Role;
import com.lodh8.starter.shared.models.enums.TipoAcesso;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleProviderPort roleProvider;

    @Override
    public Collection<Role> findAll() {
        return roleProvider.findAll();
    }

    @Override
    public Collection<Role> findAllByTipo(TipoAcesso tipo) {
        return roleProvider.findAllByTipo(tipo);
    }

}
