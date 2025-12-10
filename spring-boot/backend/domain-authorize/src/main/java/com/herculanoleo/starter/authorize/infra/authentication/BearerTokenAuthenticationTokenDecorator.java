package com.herculanoleo.starter.authorize.infra.authentication;

import com.herculanoleo.starter.authorize.domain.OAuthUser;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;

import java.io.Serial;
import java.util.Collection;
import java.util.List;

public class BearerTokenAuthenticationTokenDecorator extends BearerTokenAuthenticationToken implements OAuthUserDecorator {

    @Serial
    private static final long serialVersionUID = -2806306569585052092L;

    @Getter
    private final OAuthUser user;

    private final BearerTokenAuthenticationToken nestedAuthentication;

    private final Collection<GrantedAuthority> authorities;

    public BearerTokenAuthenticationTokenDecorator(
            BearerTokenAuthenticationToken authentication,
            OAuthUser user,
            Collection<? extends GrantedAuthority> authorities
    ) {
        super(authentication.getToken());
        this.nestedAuthentication = authentication;
        this.nestedAuthentication.setAuthenticated(true);
        this.authorities = List.copyOf(authorities);
        this.user = user;
    }

    public OAuthUser getOAuthUser() {
        return user;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public Object getCredentials() {
        return this.nestedAuthentication.getCredentials();
    }

    @Override
    public Object getDetails() {
        return this.nestedAuthentication.getDetails();
    }

    @Override
    public Object getPrincipal() {
        return this.nestedAuthentication.getPrincipal();
    }

    @Override
    public boolean isAuthenticated() {
        return this.nestedAuthentication.isAuthenticated();
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.nestedAuthentication.setAuthenticated(isAuthenticated);
    }

    @Override
    public String getName() {
        return this.nestedAuthentication.getName();
    }

}
