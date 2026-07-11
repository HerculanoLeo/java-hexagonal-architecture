package com.lodh8.starter.location.cep.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CEPDTO {

    private Long municipioId;

    private Long estadoId;

    private String cep;

    private String logradouro;

    private String bairro;

    private String ibge;

}
