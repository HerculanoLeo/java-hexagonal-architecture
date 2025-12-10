package com.herculanoleo.starter.location.estado.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstadoSearchDTO {

    private String nome;

    private String status;

}
