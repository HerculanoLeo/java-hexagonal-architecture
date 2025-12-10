package com.herculanoleo.starter.identity.roles.domain;

import com.herculanoleo.starter.shared.models.enums.TipoAcesso;

public record Role(
        String id,
        String nome,
        TipoAcesso tipo
) {
}
