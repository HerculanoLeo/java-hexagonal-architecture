package com.herculanoleo.starter.authorize.infra.oauth;

import com.herculanoleo.starter.authorize.app.port.OauthUserProviderPort;
import com.herculanoleo.starter.authorize.domain.OAuthUser;
import com.herculanoleo.starter.identity.infra.attribute.KeycloakAttributes;
import com.herculanoleo.starter.shared.models.enums.TipoAcesso;
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
