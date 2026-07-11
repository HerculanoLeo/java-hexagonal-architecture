package com.lodh8.starter.identity.grupos.infra.keycloak;

import com.lodh8.starter.identity.grupos.domain.Grupo;
import com.lodh8.starter.identity.grupos.domain.GrupoRegister;
import com.lodh8.starter.identity.grupos.domain.GrupoSearch;
import com.lodh8.starter.identity.grupos.domain.GrupoUpdate;
import com.lodh8.starter.identity.grupos.infra.GrupoMapper;
import com.lodh8.starter.identity.roles.app.port.RoleProviderPort;
import com.lodh8.starter.identity.roles.domain.Role;
import com.lodh8.starter.identity.roles.infra.RoleMapper;
import com.lodh8.starter.shared.models.enums.TipoAcesso;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
@ExtendWith(MockitoExtension.class)
class GrupoKeycloakAdapterTest {

    private UsersResource usersResource;

    private GroupsResource groupsResource;

    private GrupoMapper grupoMapper;

    private RoleProviderPort roleProvider;

    private RoleMapper roleMapper;

    private GrupoKeycloakAdapter adapter;

    private GroupResource mockGroupResource;

    private UserResource mockUserResource;

    private RoleMappingResource mockRoleMapping;

    private RoleScopeResource mockRoleScope;

    @BeforeEach
    public void setUp() {
        this.usersResource = mock(UsersResource.class);
        this.groupsResource = mock(GroupsResource.class);
        this.grupoMapper = mock(GrupoMapper.class);
        this.roleProvider = mock(RoleProviderPort.class);
        this.roleMapper = mock(RoleMapper.class);
        this.mockGroupResource = mock(GroupResource.class);
        this.mockUserResource = mock(UserResource.class);
        this.mockRoleMapping = mock(RoleMappingResource.class);
        this.mockRoleScope = mock(RoleScopeResource.class);

        this.adapter = new GrupoKeycloakAdapter(
                this.usersResource,
                this.groupsResource,
                this.grupoMapper,
                this.roleProvider,
                this.roleMapper
        );
    }

    @Test
    public void test_findAll_with_full_query_and_name_filter() {
        String tipoValor = "CLIENTE_VALUE";
        UUID relacionadoId = UUID.randomUUID();
        String nomeSearch = "Admin";

        TipoAcesso mockTipo = mock(TipoAcesso.class);
        when(mockTipo.getValue()).thenReturn(tipoValor);

        GrupoSearch mockRequest = mock(GrupoSearch.class);
        when(mockRequest.getTipo()).thenReturn(mockTipo);
        when(mockRequest.getRelacionadoId()).thenReturn(relacionadoId);
        when(mockRequest.getNome()).thenReturn(nomeSearch);

        GroupRepresentation rep1 = mock(GroupRepresentation.class);
        GroupRepresentation rep2 = mock(GroupRepresentation.class);

        Grupo grupo1 = mock(Grupo.class);
        when(grupo1.nome()).thenReturn("Grupo Admin");
        Grupo grupo2 = mock(Grupo.class);
        when(grupo2.nome()).thenReturn("Grupo User");

        String expectedQuery = "%s:%s %s:%s".formatted(TipoAcesso.APPLICATION_TYPE_KEY, tipoValor, TipoAcesso.APPLICATION_RELACIONADO_ID_KEY, relacionadoId.toString());
        when(this.groupsResource.query(expectedQuery, true, null, null, false))
                .thenReturn(List.of(rep1, rep2));

        when(this.grupoMapper.dto(rep1)).thenReturn(grupo1);
        when(this.grupoMapper.dto(rep2)).thenReturn(grupo2);

        Collection<Grupo> result = this.adapter.findAll(mockRequest);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(grupo1));
        assertFalse(result.contains(grupo2));

        verify(this.groupsResource, times(1)).query(expectedQuery, true, null, null, false);

        verify(this.grupoMapper, times(1)).dto(rep1);
        verify(this.grupoMapper, times(1)).dto(rep2);
    }

    @Test
    public void test_findAll_with_empty_query_and_no_name_filter() {
        GrupoSearch mockRequest = mock(GrupoSearch.class);
        when(mockRequest.getTipo()).thenReturn(null);
        when(mockRequest.getRelacionadoId()).thenReturn(null);
        when(mockRequest.getNome()).thenReturn(" ");

        GroupRepresentation rep1 = mock(GroupRepresentation.class);
        GroupRepresentation rep2 = mock(GroupRepresentation.class);
        Grupo grupo1 = mock(Grupo.class);
        Grupo grupo2 = mock(Grupo.class);

        String expectedQuery = "";
        when(this.groupsResource.query(expectedQuery, true, null, null, false))
                .thenReturn(List.of(rep1, rep2));

        when(this.grupoMapper.dto(rep1)).thenReturn(grupo1);
        when(this.grupoMapper.dto(rep2)).thenReturn(grupo2);

        Collection<Grupo> result = this.adapter.findAll(mockRequest);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(grupo1));
        assertTrue(result.contains(grupo2));

        verify(this.groupsResource, times(1)).query(expectedQuery, true, null, null, false);

        verify(this.grupoMapper, times(2)).dto(any(GroupRepresentation.class));

        verify(grupo1, never()).nome();
        verify(grupo2, never()).nome();
    }

    @Test
    public void test_findById_should_return_grupo_with_roles_when_found() {
        String groupId = "group-123";
        String roleName1 = "ROLE_A";
        String roleName2 = "ROLE_B";

        GroupRepresentation groupRep = new GroupRepresentation();
        RoleRepresentation roleRep1 = new RoleRepresentation();
        roleRep1.setName(roleName1);
        RoleRepresentation roleRep2 = new RoleRepresentation();
        roleRep2.setName(roleName2);

        when(this.groupsResource.group(groupId)).thenReturn(this.mockGroupResource);
        when(this.mockGroupResource.toRepresentation()).thenReturn(groupRep);
        when(this.mockGroupResource.roles()).thenReturn(this.mockRoleMapping);
        when(this.mockRoleMapping.realmLevel()).thenReturn(this.mockRoleScope);
        when(this.mockRoleScope.listAll()).thenReturn(List.of(roleRep1, roleRep2));

        Grupo mockGrupoFinal = mock(Grupo.class);
        when(this.grupoMapper.dto(any(GroupRepresentation.class))).thenReturn(mockGrupoFinal);

        Optional<Grupo> result = this.adapter.findById(groupId);

        assertTrue(result.isPresent());
        assertEquals(mockGrupoFinal, result.get());

        verify(this.groupsResource, times(1)).group(groupId);

        ArgumentCaptor<GroupRepresentation> captor = ArgumentCaptor.forClass(GroupRepresentation.class);
        verify(this.grupoMapper, times(1)).dto(captor.capture());

        GroupRepresentation capturedRep = captor.getValue();
        assertNotNull(capturedRep.getRealmRoles());
        assertEquals(2, capturedRep.getRealmRoles().size());
        assertTrue(capturedRep.getRealmRoles().containsAll(List.of(roleName1, roleName2)));
    }

    @Test
    public void test_findById_should_return_empty_on_404_NotFound() {
        String groupId = "not-found-id";

        var notFoundException = new HttpClientErrorException(HttpStatus.NOT_FOUND);
        when(this.groupsResource.group(groupId)).thenThrow(notFoundException);

        Optional<Grupo> result = this.adapter.findById(groupId);

        assertTrue(result.isEmpty());

        verify(this.grupoMapper, never()).dto(any());
    }

    @Test
    public void test_findById_should_rethrow_exception_on_other_errors() {
        String groupId = "error-id";

        var serverErrorException = new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
        when(this.groupsResource.group(groupId)).thenThrow(serverErrorException);

        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> this.adapter.findById(groupId));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        verify(this.grupoMapper, never()).dto(any());
    }

    @Test
    void findByIdentityId_whenFound_shouldMapAndReturnGrupo() {
        String identityId = "user-123";
        var groupRep = mock(GroupRepresentation.class);
        var expectedGrupo = mock(Grupo.class);

        when(usersResource.get(identityId)).thenReturn(mockUserResource);
        when(mockUserResource.groups(0, 1, false)).thenReturn(List.of(groupRep));
        when(grupoMapper.dto(groupRep)).thenReturn(expectedGrupo);

        Optional<Grupo> result = adapter.findByIdentityId(identityId);

        assertTrue(result.isPresent());
        assertEquals(expectedGrupo, result.get());
        verify(usersResource).get(identityId);
        verify(mockUserResource).groups(0, 1, false);
        verify(grupoMapper).dto(groupRep);
    }

    @Test
    void findByIdentityId_whenUserNotFound_shouldReturnEmpty() {
        String identityId = "not-found-user";
        when(usersResource.get(identityId)).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        Optional<Grupo> result = adapter.findByIdentityId(identityId);

        assertTrue(result.isEmpty());
        verify(grupoMapper, never()).dto(any());
    }

    @Test
    void findByIdentityId_whenUserHasNoGroups_shouldReturnEmpty() {
        String identityId = "user-no-groups";
        when(usersResource.get(identityId)).thenReturn(mockUserResource);
        when(mockUserResource.groups(0, 1, false)).thenReturn(List.of());

        Optional<Grupo> result = adapter.findByIdentityId(identityId);

        assertTrue(result.isEmpty());
        verify(grupoMapper, never()).dto(any());
    }

    @Test
    void findByIdentityId_whenOtherError_shouldRethrow() {
        String identityId = "error-user";
        when(usersResource.get(identityId)).thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        assertThrows(HttpClientErrorException.class, () -> adapter.findByIdentityId(identityId));
    }

    @Test
    public void test_register_should_create_and_return_grupo() throws Exception {
        GrupoRegister mockRegister = mock(GrupoRegister.class);
        GroupRepresentation mockRep = new GroupRepresentation();
        String newGroupId = "new-group-id";
        URI location = new URI("/admin/realms/realm/groups/" + newGroupId);

        when(this.grupoMapper.representation(mockRegister)).thenReturn(mockRep);

        Response mockResponse = mock(Response.class);
        when(mockResponse.getLocation()).thenReturn(location);
        when(this.groupsResource.add(mockRep)).thenReturn(mockResponse);

        GroupRepresentation finalRep = new GroupRepresentation();
        when(this.groupsResource.group(newGroupId)).thenReturn(this.mockGroupResource);
        when(this.mockGroupResource.toRepresentation()).thenReturn(finalRep);

        Grupo finalGrupo = mock(Grupo.class);
        when(this.grupoMapper.dto(finalRep)).thenReturn(finalGrupo);

        Grupo result = this.adapter.register(mockRegister);

        assertEquals(finalGrupo, result);

        verify(this.groupsResource, times(1)).add(mockRep);
        verify(this.groupsResource, times(1)).group(newGroupId);
        verify(this.grupoMapper, times(1)).dto(finalRep);
    }

    @Test
    public void test_update_should_modify_group_name() {
        String groupId = "group-to-update";
        String newName = "Updated Name";
        GrupoUpdate mockUpdate = new GrupoUpdate(newName, List.of());
        GroupRepresentation mockRep = new GroupRepresentation();
        mockRep.setName("Old Name");

        when(this.groupsResource.group(groupId)).thenReturn(this.mockGroupResource);
        when(this.mockGroupResource.toRepresentation()).thenReturn(mockRep);

        this.adapter.update(groupId, mockUpdate);

        ArgumentCaptor<GroupRepresentation> captor = ArgumentCaptor.forClass(GroupRepresentation.class);
        verify(this.mockGroupResource, times(1)).update(captor.capture());

        assertEquals(newName, captor.getValue().getName());
    }

    @Test
    public void test_delete_should_remove_group() {
        String groupId = "group-to-delete";
        when(this.groupsResource.group(groupId)).thenReturn(this.mockGroupResource);

        this.adapter.delete(groupId);

        verify(this.mockGroupResource, times(1)).remove();
    }

    @Test
    public void test_hasMembers_whenMembersExist_shouldReturnTrue() {
        String groupId = "group-with-members";
        when(this.groupsResource.group(groupId)).thenReturn(this.mockGroupResource);
        when(this.mockGroupResource.members(0, 1)).thenReturn(List.of(mock(org.keycloak.representations.idm.UserRepresentation.class)));

        assertTrue(this.adapter.hasMembers(groupId));
    }

    @Test
    public void test_hasMembers_whenEmpty_shouldReturnFalse() {
        String groupId = "empty-group";
        when(this.groupsResource.group(groupId)).thenReturn(this.mockGroupResource);
        when(this.mockGroupResource.members(0, 1)).thenReturn(List.of());

        assertFalse(this.adapter.hasMembers(groupId));
    }

    @Test
    public void test_updateRoles_should_add_and_remove_roles() {
        String groupId = "group-roles-update";
        TipoAcesso tipo = TipoAcesso.USUARIO_SISTEMA;
        Collection<String> newRoles = List.of("ROLE_NEW", "ROLE_EXISTING");

        Role roleExisting = mock(Role.class);
        Role roleToRemove = mock(Role.class);
        Role roleNew = mock(Role.class);
        when(this.roleProvider.findAllByTipo(tipo)).thenReturn(List.of(roleExisting, roleToRemove, roleNew));

        RoleRepresentation repExisting = new RoleRepresentation();
        repExisting.setName("ROLE_EXISTING");
        RoleRepresentation repToRemove = new RoleRepresentation();
        repToRemove.setName("ROLE_TO_REMOVE");
        RoleRepresentation repNew = new RoleRepresentation();
        repNew.setName("ROLE_NEW");

        when(this.roleMapper.representation(roleExisting)).thenReturn(repExisting);
        when(this.roleMapper.representation(roleToRemove)).thenReturn(repToRemove);
        when(this.roleMapper.representation(roleNew)).thenReturn(repNew);

        when(this.groupsResource.group(groupId)).thenReturn(this.mockGroupResource);
        when(this.mockGroupResource.roles()).thenReturn(this.mockRoleMapping);
        when(this.mockRoleMapping.realmLevel()).thenReturn(this.mockRoleScope);

        this.adapter.updateRoles(groupId, tipo, newRoles);

        ArgumentCaptor<List<RoleRepresentation>> removeCaptor = ArgumentCaptor.forClass(List.class);
        verify(this.mockRoleScope, times(1)).remove(removeCaptor.capture());
        assertEquals(1, removeCaptor.getValue().size());
        assertEquals("ROLE_TO_REMOVE", removeCaptor.getValue().getFirst().getName());

        ArgumentCaptor<List<RoleRepresentation>> addCaptor = ArgumentCaptor.forClass(List.class);
        verify(this.mockRoleScope, times(1)).add(addCaptor.capture());
        assertEquals(2, addCaptor.getValue().size());
        assertTrue(addCaptor.getValue().stream().anyMatch(r -> r.getName().equals("ROLE_NEW")));
        assertTrue(addCaptor.getValue().stream().anyMatch(r -> r.getName().equals("ROLE_EXISTING")));
    }

    @Test
    public void test_existsByName_should_return_true_when_group_exists_with_exact_name() {
        String groupName = "ExistingGroup";
        GroupRepresentation mockRep = new GroupRepresentation();
        mockRep.setName(groupName);

        when(this.groupsResource.groups(groupName, 0, 1)).thenReturn(List.of(mockRep));

        assertTrue(this.adapter.existsByName(groupName));
    }

    @Test
    public void test_existsByName_should_return_false_when_group_exists_with_different_case() {
        String searchName = "existinggroup";
        String actualName = "ExistingGroup";
        GroupRepresentation mockRep = new GroupRepresentation();
        mockRep.setName(actualName);

        when(this.groupsResource.groups(searchName, 0, 1)).thenReturn(List.of(mockRep));

        assertFalse(this.adapter.existsByName(searchName));
    }

    @Test
    public void test_existsByName_should_return_false_when_group_does_not_exist() {
        String groupName = "NonExistingGroup";
        when(this.groupsResource.groups(groupName, 0, 1)).thenReturn(List.of());

        assertFalse(this.adapter.existsByName(groupName));
    }
}
