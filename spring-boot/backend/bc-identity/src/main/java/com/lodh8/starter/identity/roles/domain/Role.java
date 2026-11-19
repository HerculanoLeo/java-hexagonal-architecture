package com.lodh8.starter.identity.roles.domain;

import com.lodh8.starter.shared.models.enums.TipoAcesso;

public record Role(
        String id,
        String nome,
        String descricao,
        TipoAcesso tipo
) {
}
