package com.lodh8.starter.location.estado.domain;

import com.lodh8.starter.shared.models.enums.EstadoSigla;
import com.lodh8.starter.shared.models.enums.Status;

public record Estado(
        Long id,
        EstadoSigla sigla,
        String nome,
        Status status
) {
}
