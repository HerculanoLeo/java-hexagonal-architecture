package com.herculanoleo.starter.authorize.app.port;

import com.herculanoleo.starter.authorize.domain.OAuthUser;

public interface OauthUserProviderPort {
    OAuthUser oauthUser();
}
