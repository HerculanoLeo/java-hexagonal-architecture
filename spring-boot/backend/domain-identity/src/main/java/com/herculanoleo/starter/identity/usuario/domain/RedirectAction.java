package com.herculanoleo.starter.identity.usuario.domain;

public record RedirectAction(
        String clientId,
        String redirectUri
) {
}
