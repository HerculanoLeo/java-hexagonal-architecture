package com.herculanoleo.starter.authorize.app.impl;

import com.herculanoleo.starter.authorize.app.port.OauthUserProviderPort;
import com.herculanoleo.starter.authorize.app.port.UsuarioAutenticadoProviderPort;
import com.herculanoleo.starter.authorize.domain.GrupoAutenticado;
import com.herculanoleo.starter.authorize.domain.OAuthUser;
import com.herculanoleo.starter.authorize.domain.UsuarioAutenticado;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioAutenticadoServiceImplTest {

    @Mock
    private OauthUserProviderPort oauthUserProviderPort;

    @Mock
    private UsuarioAutenticadoProviderPort usuarioAutenticadoProviderPort;

    @InjectMocks
    private UsuarioAutenticadoServiceImpl service;

    @Test
    void usuarioAutenticado_shouldDelegateToProviders() {
        // Arrange
        var mockOAuthUser = mock(OAuthUser.class);
        var expectedUser = mock(UsuarioAutenticado.class);

        when(oauthUserProviderPort.oauthUser()).thenReturn(mockOAuthUser);
        when(usuarioAutenticadoProviderPort.usuarioAutenticado(mockOAuthUser)).thenReturn(expectedUser);

        // Act
        UsuarioAutenticado result = service.usuarioAutenticado();

        // Assert
        assertEquals(expectedUser, result);
        verify(oauthUserProviderPort, times(1)).oauthUser();
        verify(usuarioAutenticadoProviderPort, times(1)).usuarioAutenticado(mockOAuthUser);
    }

    @Test
    void grupoAutenticado_shouldDelegateToProviders() {
        // Arrange
        var mockOAuthUser = mock(OAuthUser.class);
        var expectedGroup = mock(GrupoAutenticado.class);

        when(oauthUserProviderPort.oauthUser()).thenReturn(mockOAuthUser);
        when(usuarioAutenticadoProviderPort.grupo(mockOAuthUser)).thenReturn(expectedGroup);

        // Act
        GrupoAutenticado result = service.grupoAutenticado();

        // Assert
        assertEquals(expectedGroup, result);
        verify(oauthUserProviderPort, times(1)).oauthUser();
        verify(usuarioAutenticadoProviderPort, times(1)).grupo(mockOAuthUser);
    }
}
