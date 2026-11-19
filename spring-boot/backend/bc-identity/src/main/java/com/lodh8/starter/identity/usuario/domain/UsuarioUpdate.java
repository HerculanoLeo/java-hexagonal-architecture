package com.lodh8.starter.identity.usuario.domain;

public record UsuarioUpdate(
        boolean main,
        String nome,
        String email,
        String grupoId
) {
}
