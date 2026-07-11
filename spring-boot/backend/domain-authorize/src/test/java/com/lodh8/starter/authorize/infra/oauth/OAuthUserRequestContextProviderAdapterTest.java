package com.lodh8.starter.authorize.infra.oauth;

import com.lodh8.starter.authorize.domain.OAuthUser;
import com.lodh8.starter.authorize.infra.authentication.BearerTokenAuthenticationTokenDecorator;
import com.lodh8.starter.shared.models.enums.TipoAcesso;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OAuthUserRequestContextProviderAdapterTest {

    @InjectMocks
    private OAuthUserRequestContextProviderAdapter adapter;

    @Mock
    private SecurityContext securityContext;

    @BeforeEach
    void setUp() {
        // Mock the security context
        SecurityContextHolder.setContext(securityContext);
    }

    @AfterEach
    void tearDown() {
        // Clear the context to avoid side effects between tests
        SecurityContextHolder.clearContext();
    }

    @Test
    void oauthUser_whenAuthenticationIsOAuthUserDecorator_shouldReturnUser() {
        // Arrange
        OAuthUser expectedUser = mock(OAuthUser.class);
        BearerTokenAuthenticationTokenDecorator decorator = mock(BearerTokenAuthenticationTokenDecorator.class);
        when(decorator.getUser()).thenReturn(expectedUser);
        when(securityContext.getAuthentication()).thenReturn(decorator);

        // Act
        OAuthUser result = adapter.oauthUser();

        // Assert
        assertNotNull(result);
        assertEquals(expectedUser, result);
        verify(decorator, times(1)).getUser();
    }

    @Test
    void oauthUser_whenAuthenticationIsNotDecorator_shouldReturnAnonymousUser() {
        // Arrange
        Authentication otherAuth = new TestingAuthenticationToken("user", "password");
        when(securityContext.getAuthentication()).thenReturn(otherAuth);

        // Act
        OAuthUser result = adapter.oauthUser();

        // Assert
        assertNotNull(result);
        assertEquals(TipoAcesso.ANONYMOUS, result.type());
    }

    @Test
    void oauthUser_whenAuthenticationIsNull_shouldReturnAnonymousUser() {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(null);

        // Act
        OAuthUser result = adapter.oauthUser();

        // Assert
        assertNotNull(result);
        assertEquals(TipoAcesso.ANONYMOUS, result.type());
    }
}
