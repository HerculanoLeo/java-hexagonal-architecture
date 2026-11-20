package com.lodh8.starter.identity.roles.infra.keycloak;

import com.lodh8.starter.identity.roles.app.port.RoleProviderPort;
import com.lodh8.starter.identity.roles.domain.Role;
import com.lodh8.starter.identity.roles.infra.keycloak.enums.KeycloakDefaultRealmRole;
import com.lodh8.starter.identity.roles.infra.mapper.RoleMapper;
import com.lodh8.starter.shared.models.enums.TipoAcesso;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Strings;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleKeycloakAdapter implements RoleProviderPort {

    private final RolesResource rolesResource;

    private final RoleMapper roleMapper;

    @Override
    public Collection<Role> findAll() {
        log.info("roleKeycloak findAll inicio");
        var result = getRoles().stream().map(roleMapper::dto).toList();
        log.info("roleKeycloak findAll fim total={}", result.size());
        return result;
    }

    @Override
    public Collection<Role> findAllByTipo(TipoAcesso tipoAcesso) {
        log.info("roleKeycloak findAllByTipo inicio tipo={}", tipoAcesso);
        var result = findAll().stream().filter(r -> Objects.equals(r.tipo(), tipoAcesso)).toList();
        log.info("roleKeycloak findAllByTipo fim tipo={} total={}", tipoAcesso, result.size());
        return result;
    }

    protected List<RoleRepresentation> getRoles() {
        return this.rolesResource.list(false).stream().filter(role -> !role.getClientRole()
                && Arrays
                .stream(KeycloakDefaultRealmRole.values())
                .noneMatch(defaultRole -> Strings.CS.startsWith(role.getName(), defaultRole.getValue()))
        ).toList();
    }

}