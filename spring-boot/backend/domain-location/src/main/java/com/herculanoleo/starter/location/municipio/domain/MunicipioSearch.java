package com.herculanoleo.starter.location.municipio.domain;

import com.herculanoleo.starter.shared.models.enums.EstadoSigla;
import com.herculanoleo.starter.shared.models.enums.Status;

public record MunicipioSearch(
        String nome,
        Long estadoId,
        EstadoSigla sigla,
        Status status
) {
}
