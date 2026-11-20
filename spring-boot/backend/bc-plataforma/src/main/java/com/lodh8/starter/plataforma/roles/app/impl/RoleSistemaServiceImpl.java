package com.lodh8.starter.plataforma.roles.app.impl;

import com.lodh8.starter.identity.roles.app.RoleService;
import com.lodh8.starter.plataforma.roles.app.RoleSistemaService;
import com.lodh8.starter.plataforma.roles.app.port.RoleSistemaMapperPort;
import com.lodh8.starter.plataforma.roles.domain.RoleSistema;
import com.lodh8.starter.shared.models.enums.TipoAcesso;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleSistemaServiceImpl implements RoleSistemaService {

    private final RoleService roleService;

    private final RoleSistemaMapperPort mapper;

    @Override
    public Collection<RoleSistema> roles() {
        log.info("roleSistema roles inicio tipo={}", TipoAcesso.USUARIO_SISTEMA);
        var result = roleService.findAllByTipo(TipoAcesso.USUARIO_SISTEMA).stream().map(mapper::domain).toList();
        log.info("roleSistema roles fim total={}", result.size());
        return result;
    }
}