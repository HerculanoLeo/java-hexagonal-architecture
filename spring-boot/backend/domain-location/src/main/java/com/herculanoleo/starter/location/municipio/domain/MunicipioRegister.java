package com.herculanoleo.starter.location.municipio.domain;

import com.herculanoleo.starter.shared.models.enums.Status;

public record MunicipioRegister(
        String nome,
        Long estadoId,
        Status status
) {
}
