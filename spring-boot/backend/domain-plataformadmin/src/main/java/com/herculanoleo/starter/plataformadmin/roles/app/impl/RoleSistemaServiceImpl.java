package com.herculanoleo.starter.plataformadmin.roles.app.impl;

import com.herculanoleo.starter.identity.roles.app.RoleService;
import com.herculanoleo.starter.plataformadmin.roles.app.RoleSistemaService;
import com.herculanoleo.starter.plataformadmin.roles.app.port.RoleSistemaMapperPort;
import com.herculanoleo.starter.plataformadmin.roles.domain.RoleSistema;
import com.herculanoleo.starter.shared.models.enums.TipoAcesso;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class RoleSistemaServiceImpl implements RoleSistemaService {

    private final RoleService roleService;

    private final RoleSistemaMapperPort mapper;

    @Override
    public Collection<RoleSistema> roles() {
        return roleService.findAllByTipo(TipoAcesso.USUARIO_SISTEMA).stream().map(mapper::domain).toList();
    }
}
