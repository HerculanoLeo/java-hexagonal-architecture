package com.lodh8.starter.authorize.infra.oauth;

import com.lodh8.starter.authorize.app.port.OauthUserProviderPort;
import com.lodh8.starter.authorize.domain.OAuthUser;
import com.lodh8.starter.identity.infra.attribute.KeycloakAttributes;
import com.lodh8.starter.shared.models.enums.TipoAcesso;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthUserSingletonProviderAdapter implements OauthUserProviderPort {

    private final KeycloakAttributes attributes;

    @Override
    public OAuthUser oauthUser() {
        return new OAuthUser(attributes.clientId(), attributes.realm(), TipoAcesso.CLIENTE_SISTEMA);
    }
}
