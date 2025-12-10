package com.herculanoleo.starter.plataformadmin.usuarios.domain;

import com.herculanoleo.starter.shared.models.enums.Status;

public record UsuarioSistemaSearch(
        String nome,
        String email,
        Status status
) {
}
