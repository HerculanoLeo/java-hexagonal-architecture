package com.lodh8.starter.identity.usuario.domain;

import com.lodh8.starter.shared.models.enums.TipoAcesso;

public record Usuario(
        String id,
        String relacionadoId,
        TipoAcesso tipo,
        String nome,
        String email,
        boolean enabled,
        boolean emailVerified
) {
}
