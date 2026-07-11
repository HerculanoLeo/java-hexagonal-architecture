package com.lodh8.starter.backoffice.roles.app.impl;

import com.lodh8.starter.backoffice.roles.app.RoleSistemaService;
import com.lodh8.starter.backoffice.roles.app.port.RoleSistemaMapperPort;
import com.lodh8.starter.backoffice.roles.domain.RoleSistema;
import com.lodh8.starter.identity.roles.app.RoleService;
import com.lodh8.starter.shared.models.enums.TipoAcesso;
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
