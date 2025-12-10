package com.herculanoleo.starter.authorize.infra.authentication;

import com.herculanoleo.starter.authorize.domain.OAuthUser;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import java.io.Serial;
import java.util.Collection;

public class OAuth2AuthenticationTokenDecorator extends OAuth2AuthenticationToken implements OAuthUserDecorator {

    @Serial
    private static final long serialVersionUID = -4991678357748298234L;

    @Getter
    private final OAuthUser user;

    public OAuth2AuthenticationTokenDecorator(
            OAuth2AuthenticationToken authentication,
            OAuthUser user,
            Collection<? extends GrantedAuthority> authorities
    ) {
        super(authentication.getPrincipal(), authorities, authentication.getAuthorizedClientRegistrationId());
        this.user = user;
    }

}
