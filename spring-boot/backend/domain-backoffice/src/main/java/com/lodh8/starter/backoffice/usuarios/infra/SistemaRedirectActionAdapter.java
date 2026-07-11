package com.lodh8.starter.backoffice.usuarios.infra;

import com.lodh8.starter.backoffice.usuarios.app.port.SistemaRedirectActionPort;
import com.lodh8.starter.backoffice.usuarios.infra.attribute.SistemaAttributes;
import com.lodh8.starter.identity.infra.attribute.KeycloakAttributes;
import com.lodh8.starter.identity.usuario.domain.RedirectAction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SistemaRedirectActionAdapter implements SistemaRedirectActionPort {

    private final KeycloakAttributes keycloakAttributes;

    private final SistemaAttributes sistemaAttributes;

    @Override
    public RedirectAction getRedirectAction() {
        return new RedirectAction(keycloakAttributes.clientIdUsers(), sistemaAttributes.redirectUri());
    }
}
