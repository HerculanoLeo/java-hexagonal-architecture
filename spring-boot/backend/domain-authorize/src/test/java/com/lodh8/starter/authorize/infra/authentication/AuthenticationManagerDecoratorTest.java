package com.lodh8.starter.authorize.infra.authentication;

import com.lodh8.starter.authorize.domain.OAuthUser;
import com.lodh8.starter.authorize.domain.OAuthUserDetails;
import com.lodh8.starter.identity.infra.attribute.KeycloakAttributes;
import com.lodh8.starter.shared.models.enums.TipoAcesso;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import tools.jackson.databind.json.JsonMapper;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationManagerDecoratorTest {

    @Mock
    private KeycloakAttributes attributes;

    @Spy
    private JsonMapper objectMapper = JsonMapper.builder().build();

    @Mock
    private JwtDecoder jwtDecoder;

    @InjectMocks
    private AuthenticationManagerDecorator authManager;

    private Jwt mockJwt;
    private OidcUser mockOidcUser;
    private OidcIdToken mockIdToken;

    @BeforeEach
    void setUp() {
        // Common setup for JWT and OIDC user
        Map<String, Object> claims = Map.of(
                "sub", "test-sub",
                "typ", "Bearer",
                "azp", "test-client",
                "iss", "http://localhost/realms/test-realm",
                "realm_access", Map.of("roles", List.of("ROLE_USER"))
        );
        mockJwt = new Jwt("mock-token", Instant.now(), Instant.now().plusSeconds(60), Map.of("alg", "none"), claims);
        mockIdToken = new OidcIdToken("mock-id-token", Instant.now(), Instant.now().plusSeconds(60), claims);
        mockOidcUser = new DefaultOidcUser(Collections.emptyList(), mockIdToken);
    }

    @Test
    void authenticate_withBearerToken_shouldDecorateToken() {
        when(jwtDecoder.decode(anyString())).thenReturn(mockJwt);
        when(attributes.roleMapKeys()).thenReturn(new String[]{"realm_access"});

        BearerTokenAuthenticationToken bearerToken = new BearerTokenAuthenticationToken("mock-token");
        Authentication result = authManager.authenticate(bearerToken);

        assertInstanceOf(BearerTokenAuthenticationTokenDecorator.class, result);
        OAuthUser user = ((BearerTokenAuthenticationTokenDecorator) result).getUser();
        assertEquals("test-sub", user.userId());
        assertTrue(user.roles().contains("ROLE_USER"));
    }

    @Test
    void authenticate_withOAuth2Token_shouldDecorateToken() {
        when(jwtDecoder.decode(anyString())).thenReturn(mockJwt);
        when(attributes.roleMapKeys()).thenReturn(new String[]{"realm_access"});

        OAuth2AuthenticationToken oauthToken = new OAuth2AuthenticationToken(mockOidcUser, Collections.emptyList(), "test-reg-id");
        Authentication result = authManager.authenticate(oauthToken);

        assertInstanceOf(OAuth2AuthenticationTokenDecorator.class, result);
        OAuthUser user = ((OAuth2AuthenticationTokenDecorator) result).getUser();
        assertEquals("test-sub", user.userId());
        assertTrue(user.roles().contains("ROLE_USER"));
    }

    @Test
    void authenticate_withOtherTokenType_shouldReturnOriginal() {
        Authentication originalAuth = new TestingAuthenticationToken("user", "pass");
        Authentication result = authManager.authenticate(originalAuth);
        assertSame(originalAuth, result);
    }

    @Test
    void authenticate_withBearerAndJwtException_shouldReturnOriginal() {
        when(jwtDecoder.decode(anyString())).thenThrow(new JwtException("decoding error"));
        BearerTokenAuthenticationToken bearerToken = new BearerTokenAuthenticationToken("invalid-token");
        Authentication result = authManager.authenticate(bearerToken);
        assertSame(bearerToken, result);
    }

    @Test
    void authenticate_withOAuth2AndJwtException_shouldReturnOriginal() {
        when(jwtDecoder.decode(anyString())).thenThrow(new JwtException("decoding error"));
        OAuth2AuthenticationToken oauthToken = new OAuth2AuthenticationToken(mockOidcUser, Collections.emptyList(), "test-reg-id");
        Authentication result = authManager.authenticate(oauthToken);
        assertSame(oauthToken, result);
    }

    @Test
    void oauthUserDetails_withApplicationClaim_shouldReturnDetails() throws Exception {
        Map<String, Object> appClaim = Map.of("type", TipoAcesso.USUARIO_SISTEMA.name(), "relacionadoId", "some-id");
        OidcUserInfo userInfo = new OidcUserInfo(Map.of("application", appClaim));

        OAuthUserDetails details = authManager.oauthUserDetails(userInfo, objectMapper);

        assertEquals(TipoAcesso.USUARIO_SISTEMA, details.type());
        assertEquals("some-id", details.relacionadoId());
    }

    @Test
    void oauthUserDetails_withoutApplicationClaim_shouldReturnAnonymous() {
        OidcUserInfo userInfo = new OidcUserInfo(Map.of(StandardClaimNames.SUB.toLowerCase(), "test-sub"));
        OAuthUserDetails details = authManager.oauthUserDetails(userInfo, objectMapper);
        assertEquals(TipoAcesso.ANONYMOUS, details.type());
        assertNull(details.relacionadoId());
    }

    @Test
    void roles_mainDispatcher_withOidcUserInfo_shouldCallCorrectOverload() {
        AuthenticationManagerDecorator spyManager = spy(authManager);
        OidcUserInfo userInfo = new OidcUserInfo(Map.of("key", "value"));
        String[] keys = {"key"};

        // Use doReturn for spies
        doReturn(Set.of("dispatched")).when(spyManager).roles(userInfo, keys);

        Collection<String> result = spyManager.roles((Object) userInfo, keys);

        // Verify the dispatch happened
        verify(spyManager, times(1)).roles(userInfo, keys);
        assertEquals(Set.of("dispatched"), result);
    }

    @Test
    void roles_mainDispatcher_withNullAndDefault_shouldReturnEmptyList() {
        // Test null case
        Collection<String> resultNull = authManager.roles((Object) null, new String[]{});
        assertTrue(resultNull.isEmpty());

        // Test default case with an unsupported type
        Collection<String> resultDefault = authManager.roles("a string object", new String[]{});
        assertTrue(resultDefault.isEmpty());
    }


    @Test
    void roles_withOidcUserInfo_shouldExtractRoles() {
        OidcUserInfo userInfo = new OidcUserInfo(Map.of("key1", Map.of("roles", List.of("A", "B"))));
        Collection<String> result = authManager.roles(userInfo, new String[]{"key1"});
        assertEquals(Set.of("A", "B"), result);
    }

    @Test
    void roles_withMap_shouldExtractRoles() {
        Map<String, Object> map = Map.of("roles", List.of("C", "D"));
        Collection<String> result = authManager.roles(map, new String[]{}); // keys don't matter here
        assertEquals(Set.of("C", "D"), result);
    }

    @Test
    void roles_withList_shouldReturnList() {
        List<String> list = List.of("E", "F");
        Collection<String> result = authManager.roles(list);
        assertEquals(Set.of("E", "F"), result);
    }

    @Test
    void oauthUser_shouldConstructCorrectly() {
        when(jwtDecoder.decode("test-token")).thenReturn(mockJwt);
        AuthenticationManagerDecorator spyManager = spy(authManager);

        OidcUserInfo mockUserInfo = new OidcUserInfo(mockJwt.getClaims());
        OAuthUserDetails mockDetails = new OAuthUserDetails(TipoAcesso.USUARIO_SISTEMA, "rel-id");
        Set<String> mockRoles = Set.of("ROLE_ADMIN");

        doReturn(mockUserInfo).when(spyManager).getUserInfo(mockJwt);
        doReturn(mockRoles).when(spyManager).roles(mockUserInfo, null);
        doReturn(mockDetails).when(spyManager).oauthUserDetails(mockUserInfo, objectMapper);
        when(attributes.roleMapKeys()).thenReturn(null); // to isolate roles call

        OAuthUser result = spyManager.oauthUser("test-token", null);

        assertEquals("test-sub", result.userId());
        assertEquals("Bearer", result.tokenType());
        assertEquals("test-token", result.accessToken());
        assertEquals("test-realm", result.realm());
        assertEquals("test-client", result.clientId());
        assertEquals(mockRoles, result.roles());
        assertEquals(mockDetails, result.details());
    }

    // New tests for the overloaded authenticate method

    @Test
    void authenticate_withLogoutUri_andBearerToken_shouldDecorateToken() {
        when(jwtDecoder.decode(anyString())).thenReturn(mockJwt);
        when(attributes.roleMapKeys()).thenReturn(new String[]{"realm_access"});
        String logoutUri = "http://logout.com";

        BearerTokenAuthenticationToken bearerToken = new BearerTokenAuthenticationToken("mock-token");
        Authentication result = authManager.authenticate(bearerToken, logoutUri);

        assertInstanceOf(BearerTokenAuthenticationTokenDecorator.class, result);
        OAuthUser user = ((BearerTokenAuthenticationTokenDecorator) result).getUser();
        assertEquals("test-sub", user.userId());
        assertTrue(user.roles().contains("ROLE_USER"));
        // We can't directly verify the logoutRedirectUri on the OAuthUser,
        // but we can verify the authenticate private method was called with it.
        // This requires spying, which is more complex. The main goal is to see it doesn't break.
    }

    @Test
    void authenticate_withLogoutUri_andOAuth2Token_shouldDecorateToken() {
        when(jwtDecoder.decode(anyString())).thenReturn(mockJwt);
        when(attributes.roleMapKeys()).thenReturn(new String[]{"realm_access"});
        String logoutUri = "http://logout.com";

        OAuth2AuthenticationToken oauthToken = new OAuth2AuthenticationToken(mockOidcUser, Collections.emptyList(), "test-reg-id");
        Authentication result = authManager.authenticate(oauthToken, logoutUri);

        assertInstanceOf(OAuth2AuthenticationTokenDecorator.class, result);
        OAuthUser user = ((OAuth2AuthenticationTokenDecorator) result).getUser();
        assertEquals("test-sub", user.userId());
        assertTrue(user.roles().contains("ROLE_USER"));
    }

    @Test
    void authenticate_withLogoutUri_andOtherTokenType_shouldReturnOriginal() {
        Authentication originalAuth = new TestingAuthenticationToken("user", "pass");
        String logoutUri = "http://logout.com";
        Authentication result = authManager.authenticate(originalAuth, logoutUri);
        assertSame(originalAuth, result);
    }

    @Test
    void authenticate_withLogoutUri_andBearerAndJwtException_shouldReturnOriginal() {
        when(jwtDecoder.decode(anyString())).thenThrow(new JwtException("decoding error"));
        BearerTokenAuthenticationToken bearerToken = new BearerTokenAuthenticationToken("invalid-token");
        String logoutUri = "http://logout.com";
        Authentication result = authManager.authenticate(bearerToken, logoutUri);
        assertSame(bearerToken, result);
    }

    @Test
    void authenticate_withLogoutUri_andOAuth2AndJwtException_shouldReturnOriginal() {
        when(jwtDecoder.decode(anyString())).thenThrow(new JwtException("decoding error"));
        OAuth2AuthenticationToken oauthToken = new OAuth2AuthenticationToken(mockOidcUser, Collections.emptyList(), "test-reg-id");
        String logoutUri = "http://logout.com";
        Authentication result = authManager.authenticate(oauthToken, logoutUri);
        assertSame(oauthToken, result);
    }
}
