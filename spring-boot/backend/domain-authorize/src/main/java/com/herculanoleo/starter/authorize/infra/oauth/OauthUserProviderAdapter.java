package com.herculanoleo.starter.authorize.infra.oauth;

import com.herculanoleo.starter.authorize.app.port.OauthUserProviderPort;
import com.herculanoleo.starter.authorize.domain.OAuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

@Service
@RequiredArgsConstructor
@Primary
public class OauthUserProviderAdapter implements OauthUserProviderPort {

    private final OAuthUserRequestContextProviderAdapter requestProvider;

    private final OAuthUserSingletonProviderAdapter singletonProvider;

    @Override
    public OAuthUser oauthUser() {
        if (RequestContextHolder.getRequestAttributes() != null) {
            return requestProvider.oauthUser();
        }
        return singletonProvider.oauthUser();
    }
}
