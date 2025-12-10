package com.herculanoleo.starter.authorize.infra.authentication;

import com.herculanoleo.starter.authorize.domain.OAuthUser;

public interface OAuthUserDecorator {
    OAuthUser getUser();
}
