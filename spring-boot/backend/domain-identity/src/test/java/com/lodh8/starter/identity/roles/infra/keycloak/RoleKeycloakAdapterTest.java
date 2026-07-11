package com.lodh8.starter.identity.roles.infra.keycloak;

import com.lodh8.starter.identity.roles.domain.Role;
import com.lodh8.starter.identity.roles.infra.RoleMapper;
import com.lodh8.starter.shared.models.enums.TipoAcesso;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleKeycloakAdapterTest {

    private RolesResource rolesResource;

    private RoleMapper roleMapper;

    private RoleKeycloakAdapter adapter;

    private RoleRepresentation roleToKeep;

    private RoleRepresentation clientRole;

    private RoleRepresentation defaultRole;

    private RoleRepresentation defaultRoleStartsWith;

    private List<RoleRepresentation> allRolesFromKeycloak;

    @BeforeEach
    public void setUp() {
        this.rolesResource = mock(RolesResource.class);
        this.roleMapper = mock(RoleMapper.class);
        this.adapter = spy(new RoleKeycloakAdapter(this.rolesResource, this.roleMapper));

        this.roleToKeep = new RoleRepresentation();
        this.roleToKeep.setName("ROLE_CUSTOM_USER");
        this.roleToKeep.setClientRole(false);

        this.clientRole = new RoleRepresentation();
        this.clientRole.setName("account-role");
        this.clientRole.setClientRole(true);

        this.defaultRole = new RoleRepresentation();
        this.defaultRole.setName("uma_authorization");
        this.defaultRole.setClientRole(false);

        this.defaultRoleStartsWith = new RoleRepresentation();
        this.defaultRoleStartsWith.setName("offline_access_special");
        this.defaultRoleStartsWith.setClientRole(false);

        this.allRolesFromKeycloak = List.of(
                roleToKeep,
                clientRole,
                defaultRole,
                defaultRoleStartsWith
        );
    }

    @Test
    public void test_findAll_should_return_filtered_and_mapped_roles() {
        RoleRepresentation roleToKeep = new RoleRepresentation();
        roleToKeep.setName("ROLE_CUSTOM_USER");
        roleToKeep.setClientRole(false);

        RoleRepresentation clientRole = new RoleRepresentation();
        clientRole.setName("account-role");
        clientRole.setClientRole(true);

        RoleRepresentation defaultRole = new RoleRepresentation();
        defaultRole.setName("uma_authorization");
        defaultRole.setClientRole(false);

        RoleRepresentation defaultRoleStartsWith = new RoleRepresentation();
        defaultRoleStartsWith.setName("offline_access_special");
        defaultRoleStartsWith.setClientRole(false);

        List<RoleRepresentation> allRolesFromKeycloak = List.of(
                roleToKeep,
                clientRole,
                defaultRole,
                defaultRoleStartsWith
        );

        when(this.rolesResource.list(false)).thenReturn(allRolesFromKeycloak);

        Role mockDomainRole = mock(Role.class);
        when(this.roleMapper.dto(roleToKeep)).thenReturn(mockDomainRole);

        Collection<Role> result = this.adapter.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(mockDomainRole));

        verify(this.rolesResource, times(1)).list(false);

        verify(this.roleMapper, times(1)).dto(roleToKeep);

        verify(this.roleMapper, never()).dto(clientRole);
        verify(this.roleMapper, never()).dto(defaultRole);
        verify(this.roleMapper, never()).dto(defaultRoleStartsWith);
    }

    @Test
    public void test_findAllByTipo_should_return_filtered_roles() {

        Role roleClienteSistema = mock(Role.class);
        when(roleClienteSistema.tipo()).thenReturn(TipoAcesso.CLIENTE_SISTEMA);

        Role roleUsuarioSistema1 = mock(Role.class);
        when(roleUsuarioSistema1.tipo()).thenReturn(TipoAcesso.USUARIO_SISTEMA);

        Role roleUsuarioSistema2 = mock(Role.class);
        when(roleUsuarioSistema2.tipo()).thenReturn(TipoAcesso.USUARIO_SISTEMA);

        List<Role> allRoles = List.of(roleClienteSistema, roleUsuarioSistema1, roleUsuarioSistema2);

        doReturn(allRoles).when(this.adapter).findAll();

        Collection<Role> result = this.adapter.findAllByTipo(TipoAcesso.USUARIO_SISTEMA);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(roleUsuarioSistema1));
        assertTrue(result.contains(roleUsuarioSistema2));
        assertFalse(result.contains(roleClienteSistema));

        verify(this.adapter, times(1)).findAll();

        verify(roleClienteSistema, times(1)).tipo();
        verify(roleUsuarioSistema1, times(1)).tipo();
        verify(roleUsuarioSistema2, times(1)).tipo();
    }

    @Test
    public void test_getRoles_should_filter_correctly_when_called_directly() {
        when(this.rolesResource.list(false)).thenReturn(this.allRolesFromKeycloak);

        List<RoleRepresentation> result = this.adapter.getRoles();

        assertNotNull(result);
        assertEquals(1, result.size());

        assertTrue(result.contains(this.roleToKeep));

        assertFalse(result.contains(this.clientRole));
        assertFalse(result.contains(this.defaultRole));
        assertFalse(result.contains(this.defaultRoleStartsWith));

        verify(this.rolesResource, times(1)).list(false);

        verify(this.roleMapper, never()).dto(any());
    }

}