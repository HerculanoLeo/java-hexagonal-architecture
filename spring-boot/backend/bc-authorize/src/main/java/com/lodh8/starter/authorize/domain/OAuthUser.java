package com.lodh8.starter.authorize.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lodh8.starter.shared.models.enums.TipoAcesso;

import java.util.Collection;

public record OAuthUser(
        String userId,
        String tokenType,
        String accessToken,
        String realm,
        String clientId,
        Collection<String> roles,
        OAuthUserDetails details
) {

    public OAuthUser(String clientId, String realm, TipoAcesso tipoAcesso) {
        this(null, null, null, realm, clientId, null, new OAuthUserDetails(tipoAcesso, null));
    }

    @JsonIgnore
    public TipoAcesso type() {
        if (null != details) {
            return details.type();
        }
        return TipoAcesso.ANONYMOUS;
    }

}
