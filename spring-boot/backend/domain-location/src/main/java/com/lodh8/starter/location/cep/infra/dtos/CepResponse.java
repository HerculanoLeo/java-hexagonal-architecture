package com.lodh8.starter.location.cep.infra.dtos;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CepResponse {

    @JsonProperty("cep")
    @JsonAlias("cep")
    private String cep;

    @JsonProperty("state")
    @JsonAlias({"state", "uf", "estado"})
    private String state;

    @JsonProperty("city")
    @JsonAlias({"city", "localidade", "cidade"})
    private String city;

    @JsonProperty("neighborhood")
    @JsonAlias({"neighborhood", "bairro"})
    private String neighborhood;

    @JsonProperty("street")
    @JsonAlias({"street", "logradouro"})
    private String street;

    @JsonProperty("ibge")
    @JsonAlias({"ibge"})
    private String ibge;

    @JsonProperty("service")
    @JsonAlias("service")
    private String service;

    @JsonProperty("error")
    @JsonAlias("erro")
    private boolean error;

}
