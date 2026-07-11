package com.lodh8.starter.authorize.api.dto;

import jakarta.validation.constraints.NotBlank;

public record TrocaSenhaRequest(
        @NotBlank String senhaAtual,
        @NotBlank String novaSenha,
        @NotBlank String confirmacaoSenha
) {
}
