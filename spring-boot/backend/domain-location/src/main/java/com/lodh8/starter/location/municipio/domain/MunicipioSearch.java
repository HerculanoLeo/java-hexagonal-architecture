package com.lodh8.starter.location.municipio.domain;

import com.lodh8.starter.shared.models.enums.EstadoSigla;
import com.lodh8.starter.shared.models.enums.Status;

public record MunicipioSearch(
        String nome,
        Long estadoId,
        EstadoSigla sigla,
        Status status
) {
}
