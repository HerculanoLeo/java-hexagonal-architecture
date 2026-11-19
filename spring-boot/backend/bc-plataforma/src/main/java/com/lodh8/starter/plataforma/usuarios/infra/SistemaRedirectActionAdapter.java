package com.lodh8.starter.plataforma.usuarios.infra;

import com.lodh8.starter.identity.domain.attributes.KeycloakAttributes;
import com.lodh8.starter.identity.usuario.domain.RedirectAction;
import com.lodh8.starter.plataforma.usuarios.app.port.SistemaRedirectActionPort;
import com.lodh8.starter.plataforma.usuarios.domain.SistemaAttributes;
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
