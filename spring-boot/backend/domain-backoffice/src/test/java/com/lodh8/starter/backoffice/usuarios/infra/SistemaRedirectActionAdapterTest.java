package com.lodh8.starter.backoffice.usuarios.infra;

import com.lodh8.starter.backoffice.usuarios.infra.attribute.SistemaAttributes;
import com.lodh8.starter.identity.infra.attribute.KeycloakAttributes;
import com.lodh8.starter.identity.usuario.domain.RedirectAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SistemaRedirectActionAdapterTest {

    private KeycloakAttributes keycloakAttributes;

    private SistemaAttributes sistemaAttributes;

    private SistemaRedirectActionAdapter adapter;

    @BeforeEach
    void setUp() {
        keycloakAttributes = mock(KeycloakAttributes.class);
        sistemaAttributes = mock(SistemaAttributes.class);
        adapter = new SistemaRedirectActionAdapter(keycloakAttributes, sistemaAttributes);
    }

    @Test
    void getRedirectAction_shouldReturnCorrectRedirectAction() {
        String expectedClientId = "test-client-id";
        String expectedRedirectUri = "http://test.com/redirect";

        when(keycloakAttributes.clientIdUsers()).thenReturn(expectedClientId);
        when(sistemaAttributes.redirectUri()).thenReturn(expectedRedirectUri);

        RedirectAction result = adapter.getRedirectAction();

        assertEquals(expectedClientId, result.clientId());
        assertEquals(expectedRedirectUri, result.redirectUri());
    }
}
