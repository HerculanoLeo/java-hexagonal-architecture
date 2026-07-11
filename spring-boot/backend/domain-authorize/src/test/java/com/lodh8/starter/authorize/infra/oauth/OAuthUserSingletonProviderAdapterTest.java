package com.lodh8.starter.authorize.infra.oauth;

import com.lodh8.starter.authorize.domain.OAuthUser;
import com.lodh8.starter.identity.infra.attribute.KeycloakAttributes;
import com.lodh8.starter.shared.models.enums.TipoAcesso;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OAuthUserSingletonProviderAdapterTest {

    @Mock
    private KeycloakAttributes attributes;

    @InjectMocks
    private OAuthUserSingletonProviderAdapter adapter;

    @Test
    void oauthUser_shouldReturnSystemUserWithAttributes() {
        // Arrange
        String expectedClientId = "system-client";
        String expectedRealm = "system-realm";
        when(attributes.clientId()).thenReturn(expectedClientId);
        when(attributes.realm()).thenReturn(expectedRealm);

        // Act
        OAuthUser result = adapter.oauthUser();

        // Assert
        assertNotNull(result);
        assertEquals(expectedClientId, result.clientId());
        assertEquals(expectedRealm, result.realm());
        assertEquals(TipoAcesso.CLIENTE_SISTEMA, result.type());
    }
}
