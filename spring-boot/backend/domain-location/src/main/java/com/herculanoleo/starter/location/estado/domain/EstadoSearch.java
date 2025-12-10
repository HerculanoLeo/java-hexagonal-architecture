package com.herculanoleo.starter.location.estado.domain;

import com.herculanoleo.starter.shared.models.enums.Status;

public record EstadoSearch(
        String nome,
        Status status
) {
}
