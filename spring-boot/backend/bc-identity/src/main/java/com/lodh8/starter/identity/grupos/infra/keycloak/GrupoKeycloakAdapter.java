package com.lodh8.starter.identity.grupos.infra.keycloak;

import com.lodh8.starter.identity.grupos.app.port.GrupoProviderPort;
import com.lodh8.starter.identity.grupos.domain.Grupo;
import com.lodh8.starter.identity.grupos.domain.GrupoRegister;
import com.lodh8.starter.identity.grupos.domain.GrupoSearch;
import com.lodh8.starter.identity.grupos.domain.GrupoUpdate;
import com.lodh8.starter.identity.grupos.infra.mapper.GrupoMapper;
import com.lodh8.starter.identity.roles.app.port.RoleProviderPort;
import com.lodh8.starter.identity.roles.infra.mapper.RoleMapper;
import com.lodh8.starter.shared.models.enums.TipoAcesso;
import com.lodh8.starter.shared.utils.IdentificadorLog;
import com.lodh8.starter.shared.utils.TakeLastPathSegment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.keycloak.admin.client.resource.GroupsResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GrupoKeycloakAdapter implements GrupoProviderPort {

    private final UsersResource usersResource;

    private final GroupsResource groupsResource;

    private final GrupoMapper grupoMapper;

    private final RoleProviderPort roleProvider;

    private final RoleMapper roleMapper;

    @Override
    public Collection<Grupo> findAll(GrupoSearch requestEntity) {
        log.info("grupoKeycloak findAll inicio");
        log.debug("grupoKeycloak findAll inicio search={}", requestEntity);
        var query = new StringBuilder();

        if (null != requestEntity.getTipo()) {
            query.append("%s:%s ".formatted(TipoAcesso.APPLICATION_TYPE_KEY, requestEntity.getTipo().getValue()));
        }

        if (null != requestEntity.getRelacionadoId()) {
            query.append("%s:%s ".formatted(TipoAcesso.APPLICATION_RELACIONADO_ID_KEY, requestEntity.getRelacionadoId()));
        }

        var grupos = this.groupsResource.query(query.toString().trim(), true, null, null, false)
                .stream()
                .map(grupoMapper::dto)
                .toList();

        var search = requestEntity.getNome();

        Collection<Grupo> result;
        if (StringUtils.isNotBlank(search)) {
            result = grupos.stream().filter(grupo -> Strings.CI.contains(grupo.nome(), search)).toList();
        } else {
            result = grupos;
        }
        log.info("grupoKeycloak findAll fim total={}", result.size());
        return result;
    }

    @Override
    public Optional<Grupo> findById(String id) {
        log.info("grupoKeycloak findById inicio id={}", IdentificadorLog.mascarar(id));
        log.debug("grupoKeycloak findById inicio id={}", id);
        try {
            var groupResource = this.groupsResource.group(id);
            var representation = groupResource.toRepresentation();
            representation.setRealmRoles(
                    groupResource.roles().realmLevel().listAll().stream().map(RoleRepresentation::getName).toList()
            );
            log.info("grupoKeycloak findById fim id={}", IdentificadorLog.mascarar(id));
            return Optional.ofNullable(grupoMapper.dto(representation));
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                log.warn("grupoKeycloak findById id={} resultado=nao_encontrado", IdentificadorLog.mascarar(id));
                return Optional.empty();
            }
            log.error("grupoKeycloak findById id={} erro", IdentificadorLog.mascarar(id), ex);
            throw ex;
        }
    }

    @Override
    public Optional<Grupo> findByIdentityId(String identityId) {
        log.info("grupoKeycloak findByIdentityId inicio identityId={}", IdentificadorLog.mascarar(identityId));
        log.debug("grupoKeycloak findByIdentityId inicio identityId={}", identityId);
        try {
            var result = this.usersResource.get(identityId).groups(0, 1, false)
                    .stream()
                    .findFirst()
                    .map(grupoMapper::dto);
            if (result.isEmpty()) {
                log.warn("grupoKeycloak findByIdentityId identityId={} resultado=nao_encontrado", IdentificadorLog.mascarar(identityId));
            } else {
                log.info("grupoKeycloak findByIdentityId fim identityId={}", IdentificadorLog.mascarar(identityId));
            }
            return result;
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                log.warn("grupoKeycloak findByIdentityId identityId={} resultado=nao_encontrado", IdentificadorLog.mascarar(identityId));
                return Optional.empty();
            }
            log.error("grupoKeycloak findByIdentityId identityId={} erro", IdentificadorLog.mascarar(identityId), ex);
            throw ex;
        }
    }

    @Override
    public Grupo register(GrupoRegister requestEntity) {
        log.info("grupoKeycloak register inicio nome={}", IdentificadorLog.mascarar(requestEntity.nome()));
        log.debug("grupoKeycloak register inicio nome={}", requestEntity.nome());
        var representation = this.grupoMapper.representation(requestEntity);

        try (var response = this.groupsResource.add(representation)) {
            var id = TakeLastPathSegment.fromURI(response.getLocation());
            var grupo = grupoMapper.dto(this.groupsResource.group(id).toRepresentation());
            log.info("grupoKeycloak register fim id={}", IdentificadorLog.mascarar(id));
            log.debug("grupoKeycloak register fim id={}", id);
            return grupo;
        }
    }

    @Override
    public void update(String id, GrupoUpdate requestEntity) {
        log.info("grupoKeycloak update inicio id={}", IdentificadorLog.mascarar(id));
        log.debug("grupoKeycloak update inicio id={}", id);
        var groupResource = groupsResource.group(id);
        var representation = groupResource.toRepresentation();
        representation.setName(requestEntity.nome());
        groupResource.update(representation);
        log.info("grupoKeycloak update fim id={}", IdentificadorLog.mascarar(id));
    }

    @Override
    public void delete(String id) {
        log.info("grupoKeycloak delete inicio id={}", IdentificadorLog.mascarar(id));
        log.debug("grupoKeycloak delete inicio id={}", id);
        groupsResource.group(id).remove();
        log.info("grupoKeycloak delete fim id={}", IdentificadorLog.mascarar(id));
    }

    @Override
    public boolean hasMembers(String id) {
        log.info("grupoKeycloak hasMembers inicio id={}", IdentificadorLog.mascarar(id));
        var members = groupsResource.group(id).members(0, 1);
        var has = members != null && !members.isEmpty();
        log.info("grupoKeycloak hasMembers fim id={} resultado={}", IdentificadorLog.mascarar(id), has);
        return has;
    }

    @Override
    public void updateRoles(String id, TipoAcesso tipo, Collection<String> newRoles) {
        log.info("grupoKeycloak updateRoles inicio id={} tipo={}", IdentificadorLog.mascarar(id), tipo);
        log.debug("grupoKeycloak updateRoles inicio id={} tipo={} roles={}", id, tipo, newRoles);
        var realmRoles = this.roleProvider.findAllByTipo(tipo)
                .stream()
                .map(roleMapper::representation)
                .toList();

        var rolesToRemove = realmRoles.stream()
                .filter(actual -> newRoles.stream().noneMatch(roleName -> actual.getName().equals(roleName)))
                .toList();

        var rolesToSave = realmRoles.stream()
                .filter(actual -> newRoles.stream().anyMatch(roleName -> actual.getName().equals(roleName)))
                .toList();

        var groupResource = this.groupsResource.group(id);

        groupResource.roles().realmLevel().remove(rolesToRemove);
        groupResource.roles().realmLevel().add(rolesToSave);
        log.info("grupoKeycloak updateRoles fim id={}", IdentificadorLog.mascarar(id));
    }

    @Override
    public boolean existsByName(String name) {
        log.info("grupoKeycloak existsByName inicio nome={}", IdentificadorLog.mascarar(name));
        log.debug("grupoKeycloak existsByName inicio nome={}", name);
        var groups = this.groupsResource.groups(
                name,
                0,
                1
        );

        boolean exists;
        if (!groups.isEmpty()) {
            var groupRepresentation = groups.getFirst();
            exists = Strings.CS.equals(groupRepresentation.getName(), name);
        } else {
            exists = false;
        }
        log.info("grupoKeycloak existsByName fim nome={} resultado={}", IdentificadorLog.mascarar(name), exists);
        return exists;
    }

}