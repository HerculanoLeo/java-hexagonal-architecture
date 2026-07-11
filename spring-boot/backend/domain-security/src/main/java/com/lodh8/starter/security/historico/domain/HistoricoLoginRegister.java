package com.lodh8.starter.security.historico.domain;

import com.lodh8.starter.shared.models.enums.TipoAcesso;

import java.util.UUID;

public record HistoricoLoginRegister(
        String identityId,
        UUID usuarioId,
        TipoAcesso tipo,
        String relacionadoId,
        String email,
        String nome,
        String ip,
        String userAgent,
        String sessaoBffId
) {
}
