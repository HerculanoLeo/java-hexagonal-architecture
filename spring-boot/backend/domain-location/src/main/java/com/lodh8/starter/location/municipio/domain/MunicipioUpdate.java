package com.lodh8.starter.location.municipio.domain;

import com.lodh8.starter.shared.models.enums.Status;

public record MunicipioUpdate(
        String nome,
        Long estadoId,
        Status status
) {
}
