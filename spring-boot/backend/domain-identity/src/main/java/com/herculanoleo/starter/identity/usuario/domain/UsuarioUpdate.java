package com.herculanoleo.starter.identity.usuario.domain;

public record UsuarioUpdate(
        boolean main,
        String nome,
        String email,
        String grupoId
) {
}
