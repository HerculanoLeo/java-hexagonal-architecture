package com.lodh8.starter.authorize.infra.authentication;

import com.lodh8.starter.authorize.domain.OAuthUser;

public interface OAuthUserDecorator {
    OAuthUser getUser();
}
