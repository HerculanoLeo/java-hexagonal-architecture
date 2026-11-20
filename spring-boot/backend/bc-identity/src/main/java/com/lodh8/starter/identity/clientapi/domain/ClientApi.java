package com.lodh8.starter.identity.clientapi.domain;

import com.lodh8.starter.shared.models.enums.TipoAcesso;

public record ClientApi(
        String id,
        String clientId,
        String nome,
        TipoAcesso tipo,
        boolean enabled
) {
}
