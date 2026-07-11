package com.lodh8.starter.location.estado.domain;

import com.lodh8.starter.shared.models.enums.Status;

public record EstadoSearch(
        String nome,
        Status status
) {
}
