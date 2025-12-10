package com.herculanoleo.starter.location.municipio.api.dto;

import com.herculanoleo.starter.location.estado.api.dto.EstadoDTO;
import com.herculanoleo.starter.shared.models.enums.Status;
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
