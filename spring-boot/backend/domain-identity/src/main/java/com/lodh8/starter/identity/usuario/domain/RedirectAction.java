package com.lodh8.starter.identity.usuario.domain;

public record RedirectAction(
        String clientId,
        String redirectUri
) {
}
