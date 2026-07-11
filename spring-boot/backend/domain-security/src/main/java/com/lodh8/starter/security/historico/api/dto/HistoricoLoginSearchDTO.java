package com.lodh8.starter.security.historico.api.dto;

import com.lodh8.starter.shared.models.enums.TipoAcesso;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoricoLoginSearchDTO {

    private OffsetDateTime dataEventoDe;

    private OffsetDateTime dataEventoAte;

    private String email;

    private TipoAcesso tipo;

    private String relacionadoId;

    private UUID usuarioId;

}
