package com.herculanoleo.starter.identity.roles.app.impl;

import com.herculanoleo.starter.identity.roles.app.port.RoleProviderPort;
import com.herculanoleo.starter.identity.roles.domain.Role;
import com.herculanoleo.starter.shared.models.enums.TipoAcesso;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    private RoleProviderPort roleProvider;

    private RoleServiceImpl service;

    @BeforeEach
    public void setUp() {
        this.roleProvider = mock(RoleProviderPort.class);
        this.service = new RoleServiceImpl(this.roleProvider);
    }

    @Test
    public void test_findAll_should_delegate_to_provider() {
        List<Role> mockRoles = List.of(mock(Role.class), mock(Role.class));

        when(this.roleProvider.findAll()).thenReturn(mockRoles);

        Collection<Role> result = this.service.findAll();

        assertNotNull(result);
        assertEquals(mockRoles, result);
        assertEquals(2, result.size());

        verify(this.roleProvider, times(1)).findAll();
    }

    @Test
    public void test_findAllByTipo_should_delegate_to_provider() {
        TipoAcesso tipo = TipoAcesso.USUARIO_SISTEMA;

        List<Role> mockRoles = List.of(mock(Role.class));

        when(this.roleProvider.findAllByTipo(tipo)).thenReturn(mockRoles);

        Collection<Role> result = this.service.findAllByTipo(tipo);

        assertNotNull(result);
        assertEquals(mockRoles, result);
        assertEquals(1, result.size());

        verify(this.roleProvider, times(1)).findAllByTipo(tipo);
    }
}