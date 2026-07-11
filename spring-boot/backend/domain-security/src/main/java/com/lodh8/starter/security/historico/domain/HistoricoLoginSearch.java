package com.lodh8.starter.security.historico.domain;

import com.lodh8.starter.shared.models.enums.TipoAcesso;

import java.time.OffsetDateTime;
import java.util.UUID;

public record HistoricoLoginSearch(
        OffsetDateTime dataEventoDe,
        OffsetDateTime dataEventoAte,
        String email,
        TipoAcesso tipo,
        String relacionadoId,
        UUID usuarioId
) {
}
