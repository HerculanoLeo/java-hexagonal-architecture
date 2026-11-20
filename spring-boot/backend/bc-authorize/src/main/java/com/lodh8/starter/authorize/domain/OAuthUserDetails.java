package com.lodh8.starter.authorize.domain;

import com.lodh8.starter.shared.models.enums.TipoAcesso;

public record OAuthUserDetails(
        TipoAcesso type,
        String relacionadoId
) {
}
