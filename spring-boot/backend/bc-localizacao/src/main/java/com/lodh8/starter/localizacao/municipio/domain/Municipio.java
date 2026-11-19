package com.lodh8.starter.localizacao.municipio.domain;

import com.lodh8.starter.localizacao.estado.domain.Estado;
import com.lodh8.starter.shared.models.enums.Status;

public record Municipio(
        Long id,
        String nome,
        Estado estado,
        Status status
) {
}
