package com.lodh8.starter.identity.usuario.domain;

public record TrocaSenha(
        String senhaAtual,
        String novaSenha,
        String confirmacaoSenha
) {
}
