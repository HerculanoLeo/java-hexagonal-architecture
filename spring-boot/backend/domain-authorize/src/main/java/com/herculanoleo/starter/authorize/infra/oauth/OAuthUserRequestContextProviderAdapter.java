package com.herculanoleo.starter.authorize.infra.oauth;

import com.herculanoleo.starter.authorize.app.port.OauthUserProviderPort;
import com.herculanoleo.starter.authorize.domain.OAuthUser;
import com.herculanoleo.starter.authorize.infra.authentication.OAuthUserDecorator;
import com.herculanoleo.starter.shared.models.enums.TipoAcesso;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope(proxyMode = ScopedProxyMode.TARGET_CLASS)
@RequiredArgsConstructor
public class OAuthUserRequestContextProviderAdapter implements OauthUserProviderPort {
    @Override
    public OAuthUser oauthUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof OAuthUserDecorator decorator) {
            return decorator.getUser();
        }

        return new OAuthUser(null, null, TipoAcesso.ANONYMOUS);
    }
}
