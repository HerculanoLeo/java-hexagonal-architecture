package com.herculanoleo.starter.plataformadmin.roles.app.impl;

import com.herculanoleo.starter.identity.roles.app.RoleService;
import com.herculanoleo.starter.identity.roles.domain.Role;
import com.herculanoleo.starter.plataformadmin.roles.app.port.RoleSistemaMapperPort;
import com.herculanoleo.starter.plataformadmin.roles.domain.RoleSistema;
import com.herculanoleo.starter.shared.models.enums.TipoAcesso;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleSistemaServiceImplTest {

    @Mock
    private RoleService roleService;

    @Mock
    private RoleSistemaMapperPort mapper;

    @InjectMocks
    private RoleSistemaServiceImpl roleSistemaService;

    @Test
    void roles_shouldReturnMappedRolesForUsuarioSistema() {
        // Arrange
        Role role1 = mock(Role.class);
        Role role2 = mock(Role.class);
        List<Role> identityRoles = List.of(role1, role2);

        RoleSistema roleSistema1 = mock(RoleSistema.class);
        RoleSistema roleSistema2 = mock(RoleSistema.class);

        when(roleService.findAllByTipo(TipoAcesso.USUARIO_SISTEMA)).thenReturn(identityRoles);
        when(mapper.domain(role1)).thenReturn(roleSistema1);
        when(mapper.domain(role2)).thenReturn(roleSistema2);

        // Act
        Collection<RoleSistema> result = roleSistemaService.roles();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(roleSistema1));
        assertTrue(result.contains(roleSistema2));

        // Verify interactions
        verify(roleService, times(1)).findAllByTipo(TipoAcesso.USUARIO_SISTEMA);
        verify(mapper, times(1)).domain(role1);
        verify(mapper, times(1)).domain(role2);
        verifyNoMoreInteractions(roleService, mapper);
    }
}
