package com.herculanoleo.starter.plataformadmin.usuarios.infra;

import com.herculanoleo.starter.identity.infra.attribute.KeycloakAttributes;
import com.herculanoleo.starter.identity.usuario.domain.RedirectAction;
import com.herculanoleo.starter.plataformadmin.usuarios.app.port.SistemaRedirectActionPort;
import com.herculanoleo.starter.plataformadmin.usuarios.infra.attribute.SistemaAttributes;
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
