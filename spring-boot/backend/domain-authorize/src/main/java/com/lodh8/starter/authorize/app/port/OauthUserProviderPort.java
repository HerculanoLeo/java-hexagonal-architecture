package com.lodh8.starter.authorize.app.port;

import com.lodh8.starter.authorize.domain.OAuthUser;

public interface OauthUserProviderPort {
    OAuthUser oauthUser();
}
