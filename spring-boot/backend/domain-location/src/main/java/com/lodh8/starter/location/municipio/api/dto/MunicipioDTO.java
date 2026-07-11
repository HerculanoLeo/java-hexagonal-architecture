package com.lodh8.starter.location.municipio.api.dto;

import com.lodh8.starter.location.estado.api.dto.EstadoDTO;
import com.lodh8.starter.shared.models.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MunicipioDTO {
    private Long id;

    private String nome;

    private EstadoDTO estado;

    private Status status;
}
