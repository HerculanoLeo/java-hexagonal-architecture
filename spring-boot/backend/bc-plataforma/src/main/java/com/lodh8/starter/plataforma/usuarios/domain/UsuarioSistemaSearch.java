package com.lodh8.starter.plataforma.usuarios.domain;

import com.lodh8.starter.shared.models.enums.Status;

public record UsuarioSistemaSearch(
        String nome,
        String email,
        Status status
) {
}
