package com.herculanoleo.starter.location.cep.domain;

public record CEP(
        Long municipioId,
        Long estadoId,
        String cep,
        String logradouro,
        String bairro,
        String ibge
) {
}
