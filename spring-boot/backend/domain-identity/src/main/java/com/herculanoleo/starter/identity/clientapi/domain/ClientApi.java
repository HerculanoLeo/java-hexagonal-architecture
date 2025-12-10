package com.herculanoleo.starter.identity.clientapi.domain;

import com.herculanoleo.starter.shared.models.enums.TipoAcesso;

public record ClientApi(
        String id,
        String clientId,
        String nome,
        TipoAcesso tipo,
        boolean enabled
) {
}
