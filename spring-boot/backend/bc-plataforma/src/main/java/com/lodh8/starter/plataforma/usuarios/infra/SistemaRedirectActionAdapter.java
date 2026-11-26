package com.lodh8.starter.plataforma.usuarios.infra;

import com.lodh8.starter.identity.domain.attributes.KeycloakAttributes;
import com.lodh8.starter.identity.usuario.domain.RedirectAction;
import com.lodh8.starter.plataforma.usuarios.app.port.SistemaRedirectActionPort;
import com.lodh8.starter.plataforma.usuarios.domain.SistemaAttributes;
import com.lodh8.starter.shared.utils.IdentificadorLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SistemaRedirectActionAdapter implements SistemaRedirectActionPort {

    private final KeycloakAttributes keycloakAttributes;

    private final SistemaAttributes sistemaAttributes;

    @Override
    public RedirectAction getRedirectAction() {
        log.info("sistemaRedirect getRedirectAction inicio");
        var action = new RedirectAction(keycloakAttributes.getClientIdUsers(), sistemaAttributes.getRedirectUri());
        log.debug(
                "sistemaRedirect getRedirectAction fim clientId={} redirectUri={}",
                action.clientId(),
                action.redirectUri()
        );
        log.info(
                "sistemaRedirect getRedirectAction fim clientId={}",
                IdentificadorLog.mascarar(action.clientId())
        );
        return action;
    }
}