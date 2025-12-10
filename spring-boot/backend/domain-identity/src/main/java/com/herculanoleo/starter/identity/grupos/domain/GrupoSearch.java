package com.herculanoleo.starter.identity.grupos.domain;

import com.herculanoleo.starter.shared.models.enums.TipoAcesso;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GrupoSearch {

    private String nome;

    private TipoAcesso tipo;

    private UUID relacionadoId;

}
