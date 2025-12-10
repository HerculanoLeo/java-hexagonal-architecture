package com.herculanoleo.starter.location.municipio.domain;

import com.herculanoleo.starter.shared.models.enums.Status;

public record MunicipioUpdate(
        String nome,
        Long estadoId,
        Status status
) {
}
