package com.herculanoleo.starter.location.municipio.domain;

import com.herculanoleo.starter.location.estado.domain.Estado;
import com.herculanoleo.starter.shared.models.enums.Status;

public record Municipio(
        Long id,
        String nome,
        Estado estado,
        Status status
) {
}
