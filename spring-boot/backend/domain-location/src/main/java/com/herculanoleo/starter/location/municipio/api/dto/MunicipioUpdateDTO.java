package com.herculanoleo.starter.location.municipio.api.dto;

import com.herculanoleo.starter.shared.models.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MunicipioUpdateDTO {
    private String nome;

    private Long estadoId;

    private Status status;
}
