package com.lodh8.starter.authorize.infra.authentication;

import com.lodh8.starter.authorize.domain.OAuthUser;
import com.lodh8.starter.authorize.domain.OAuthUserDetails;
import com.lodh8.starter.identity.infra.attribute.KeycloakAttributes;
import com.lodh8.starter.shared.models.enums.TipoAcesso;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

import java.util.*;

@Slf4j
@Component("authenticationManagerDecorator")
@RequiredArgsConstructor
public class AuthenticationManagerDecorator implements AuthenticationManager {

    protected static final String KEYCLOAK_APPLICATION_KEY = "application";

    private final KeycloakAttributes attributes;

    private final JsonMapper objectMapper;

    private final JwtDecoder jwtDecoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication instanceof BearerTokenAuthenticationToken bearer) {
            try {
                return authenticate(bearer, bearer.getToken(), null);
            } catch (JwtException ex) {
                log.debug(ex.getMessage(), ex);
            }
        }

        if (authentication instanceof OAuth2AuthenticationToken oauth && oauth.getPrincipal() instanceof OidcUser user) {
            try {
                return authenticate(oauth, user.getIdToken().getTokenValue(), null);
            } catch (JwtException ex) {
                log.debug(ex.getMessage(), ex);
            }
        }

        return authentication;
    }

    public Authentication authenticate(Authentication authentication, String logoutRedirectUri) throws AuthenticationException {
        if (authentication instanceof BearerTokenAuthenticationToken bearer) {
            try {
                return authenticate(bearer, bearer.getToken(), logoutRedirectUri);
            } catch (JwtException ex) {
                log.debug(ex.getMessage(), ex);
            }
        }

        if (authentication instanceof OAuth2AuthenticationToken oauth && oauth.getPrincipal() instanceof OidcUser user) {
            try {
                return authenticate(oauth, user.getIdToken().getTokenValue(), logoutRedirectUri);
            } catch (JwtException ex) {
                log.debug(ex.getMessage(), ex);
            }
        }

        return authentication;
    }

    private BearerTokenAuthenticationToken authenticate(
            BearerTokenAuthenticationToken authentication, String token, String logoutRedirectUri
    ) throws JwtException {
        var user = oauthUser(token, logoutRedirectUri);

        return new BearerTokenAuthenticationTokenDecorator(
                authentication,
                user,
                user.roles().stream().map(SimpleGrantedAuthority::new).toList()
        );
    }

    private OAuth2AuthenticationToken authenticate(
            OAuth2AuthenticationToken authentication, String token, String logoutRedirectUri
    ) throws JwtException {
        var user = oauthUser(token, logoutRedirectUri);
        return new OAuth2AuthenticationTokenDecorator(
                authentication,
                user,
                user.roles().stream().map(SimpleGrantedAuthority::new).toList()
        );
    }

    protected String tokenType(Jwt jwt) {
        return (String) jwt.getClaims().get("typ");
    }

    protected String clientId(Jwt jwt) {
        return (String) jwt.getClaims().get("azp");
    }

    @SneakyThrows
    protected OidcUserInfo getUserInfo(Jwt jwt) {
        return new OidcUserInfo(jwt.getClaims());
    }

    @SneakyThrows
    protected OAuthUserDetails oauthUserDetails(OidcUserInfo userInfo, JsonMapper objectMapper) {
        if (userInfo.getClaims().get(KEYCLOAK_APPLICATION_KEY) instanceof Map<?, ?> claim) {
            String claimAsString = objectMapper.writeValueAsString(claim);
            return objectMapper.readValue(claimAsString, OAuthUserDetails.class);
        }
        return new OAuthUserDetails(TipoAcesso.ANONYMOUS, null);
    }

    protected String realm(Jwt jwt) {
        String issuer = (String) jwt.getClaims().get("iss");
        String[] split = issuer.split("/");
        return split[split.length - 1];
    }

    protected Collection<String> roles(Object obj, String[] keys) {
        return switch (obj) {
            case OidcUserInfo userInfo -> roles(userInfo, keys);
            case Map<?, ?> map -> roles(map, keys);
            case List<?> list -> roles(list);
            case null, default -> Collections.emptyList();
        };
    }

    protected Collection<String> roles(OidcUserInfo userInfo, String[] keys) {
        Set<String> roles = new HashSet<>();
        for (String key : keys) {
            roles.addAll(roles(userInfo.getClaims().get(key), keys));
        }
        return roles;
    }

    protected Collection<String> roles(Map<?, ?> map, String[] keys) {
        Set<String> roles = new HashSet<>();
        for (Object value : map.values()) {
            roles.addAll(roles(value, keys));
        }
        return roles;
    }

    protected Collection<String> roles(List<?> list) {
        Set<String> roles = new HashSet<>();
        for (Object item : list) {
            roles.add(item.toString());
        }
        return roles;
    }

    protected OAuthUser oauthUser(String token, String logoutRedirectUri) {
        var jwt = jwtDecoder.decode(token);

        var userInfo = this.getUserInfo(jwt);
        var roles = roles(userInfo, attributes.roleMapKeys());
        var details = oauthUserDetails(userInfo, objectMapper);

        return new OAuthUser(
                userInfo.getSubject(),
                tokenType(jwt),
                token,
                realm(jwt),
                clientId(jwt),
                roles,
                details
        );
    }

}

