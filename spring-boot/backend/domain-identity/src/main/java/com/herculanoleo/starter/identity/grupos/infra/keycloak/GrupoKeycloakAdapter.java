package com.herculanoleo.starter.identity.grupos.infra.keycloak;

import com.herculanoleo.starter.identity.grupos.app.port.GrupoProviderPort;
import com.herculanoleo.starter.identity.grupos.domain.Grupo;
import com.herculanoleo.starter.identity.grupos.domain.GrupoRegister;
import com.herculanoleo.starter.identity.grupos.domain.GrupoSearch;
import com.herculanoleo.starter.identity.grupos.domain.GrupoUpdate;
import com.herculanoleo.starter.identity.grupos.infra.GrupoMapper;
import com.herculanoleo.starter.identity.roles.app.port.RoleProviderPort;
import com.herculanoleo.starter.identity.roles.infra.RoleMapper;
import com.herculanoleo.starter.shared.models.enums.TipoAcesso;
import com.herculanoleo.starter.shared.utils.TakeLastPathSegment;
import lombok.RequiredArgsConstructor;
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

        if (StringUtils.isNotBlank(search)) {
            return grupos.stream().filter(grupo -> Strings.CI.contains(grupo.nome(), search)).toList();
        }

        return grupos;
    }

    @Override
    public Optional<Grupo> findById(String id) {
        try {
            var groupResource = this.groupsResource.group(id);
            var representation = groupResource.toRepresentation();
            representation.setRealmRoles(
                    groupResource.roles().realmLevel().listAll().stream().map(RoleRepresentation::getName).toList()
            );
            return Optional.ofNullable(grupoMapper.dto(representation));
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                return Optional.empty();
            }
            throw ex;
        }
    }

    @Override
    public Optional<Grupo> findByIdentityId(String identityId) {
        try {
            return this.usersResource.get(identityId).groups(0, 1, false)
                    .stream()
                    .findFirst()
                    .map(grupoMapper::dto);
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                return Optional.empty();
            }
            throw ex;
        }
    }

    @Override
    public Grupo register(GrupoRegister requestEntity) {
        var representation = this.grupoMapper.representation(requestEntity);

        try (var response = this.groupsResource.add(representation)) {
            var id = TakeLastPathSegment.fromURI(response.getLocation());
            return grupoMapper.dto(this.groupsResource.group(id).toRepresentation());
        }
    }

    @Override
    public void update(String id, GrupoUpdate requestEntity) {
        var groupResource = groupsResource.group(id);
        var representation = groupResource.toRepresentation();
        representation.setName(requestEntity.nome());
        groupResource.update(representation);
    }

    @Override
    public void delete(String id) {
        groupsResource.group(id).remove();
    }

    @Override
    public void updateRoles(String id, TipoAcesso tipo, Collection<String> newRoles) {
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
    }

    @Override
    public boolean existsByName(String name) {
        var groups = this.groupsResource.groups(
                name,
                0,
                1
        );

        if (!groups.isEmpty()) {
            var groupRepresentation = groups.getFirst();
            return Strings.CS.equals(groupRepresentation.getName(), name);
        }

        return false;
    }

}
