package com.herculanoleo.starter.location.estado.domain;

import com.herculanoleo.starter.shared.models.enums.EstadoSigla;
import com.herculanoleo.starter.shared.models.enums.Status;

public record Estado(
        Long id,
        EstadoSigla sigla,
        String nome,
        Status status
) {
}
