package com.lodh8.starter.localizacao.municipio.domain;

import com.lodh8.starter.shared.models.enums.Status;

public record MunicipioRegister(
        String nome,
        Long estadoId,
        Status status
) {
}
