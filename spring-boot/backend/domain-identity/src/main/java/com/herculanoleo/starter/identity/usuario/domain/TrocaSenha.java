package com.herculanoleo.starter.identity.usuario.domain;

public record TrocaSenha(
        String senhaAtual,
        String novaSenha,
        String confirmacaoSenha
) {
}
