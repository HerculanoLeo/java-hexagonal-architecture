package com.herculanoleo.starter.location.estado.api.dto;

import com.herculanoleo.starter.shared.models.enums.EstadoSigla;
import com.herculanoleo.starter.shared.models.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstadoDTO {
    private Long id;
    private String nome;
    private EstadoSigla sigla;
    private Status status;
}
