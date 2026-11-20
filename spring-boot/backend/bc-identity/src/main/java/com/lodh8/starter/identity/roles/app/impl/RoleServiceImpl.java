package com.lodh8.starter.identity.roles.app.impl;

import com.lodh8.starter.identity.roles.app.RoleService;
import com.lodh8.starter.identity.roles.app.port.RoleProviderPort;
import com.lodh8.starter.identity.roles.domain.Role;
import com.lodh8.starter.shared.models.enums.TipoAcesso;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleProviderPort roleProvider;

    @Override
    public Collection<Role> findAll() {
        log.info("role findAll inicio");
        var result = roleProvider.findAll();
        log.info("role findAll fim total={}", result.size());
        return result;
    }

    @Override
    public Collection<Role> findAllByTipo(TipoAcesso tipo) {
        log.info("role findAllByTipo tipo={}", tipo);
        var result = roleProvider.findAllByTipo(tipo);
        log.info("role findAllByTipo tipo={} total={}", tipo, result.size());
        return result;
    }

}
