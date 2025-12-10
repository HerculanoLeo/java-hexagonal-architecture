package com.herculanoleo.starter.authorize.domain;

import com.herculanoleo.starter.shared.models.enums.TipoAcesso;

public record OAuthUserDetails(
        TipoAcesso type,
        String relacionadoId
) {
}
