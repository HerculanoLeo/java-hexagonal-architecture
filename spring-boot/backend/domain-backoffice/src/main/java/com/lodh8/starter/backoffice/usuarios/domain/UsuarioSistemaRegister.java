package com.lodh8.starter.backoffice.usuarios.domain;

public record UsuarioSistemaRegister(
        String identityId,
        boolean main,
        String nome,
        String email,
        String senha,
        String grupoId
) {

    public UsuarioSistemaRegister(String nome,
                                  String email,
                                  String senha,
                                  String grupoId) {
        this(null, false, nome, email, senha, grupoId);
    }

    public UsuarioSistemaRegister withIdentity(String identityId) {
        return new UsuarioSistemaRegister(identityId, main, nome, email, senha, grupoId);
    }

}
