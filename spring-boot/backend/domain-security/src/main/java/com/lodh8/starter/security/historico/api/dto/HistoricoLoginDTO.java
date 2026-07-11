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
public class HistoricoLoginDTO {

    private UUID id;

    private String identityId;

    private UUID usuarioId;

    private TipoAcesso tipo;

    private String relacionadoId;

    private String email;

    private String nome;

    private String ip;

    private String userAgent;

    private boolean sucesso;

    private OffsetDateTime dataEvento;

    private String sessaoBffId;

}
