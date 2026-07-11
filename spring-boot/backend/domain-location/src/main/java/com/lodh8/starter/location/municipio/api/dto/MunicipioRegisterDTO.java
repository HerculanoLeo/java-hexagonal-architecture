package com.lodh8.starter.location.municipio.api.dto;

import com.lodh8.starter.shared.models.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MunicipioRegisterDTO {
    private String nome;

    private Long estadoId;

    private Status status;
}
