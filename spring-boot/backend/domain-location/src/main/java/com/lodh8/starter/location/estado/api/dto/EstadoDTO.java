package com.lodh8.starter.location.estado.api.dto;

import com.lodh8.starter.shared.models.enums.EstadoSigla;
import com.lodh8.starter.shared.models.enums.Status;
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
