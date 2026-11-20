package com.lodh8.starter.authorize.api.dto;

import jakarta.validation.constraints.NotBlank;

public record UsuarioAutenticadoUpdateRequest(
        @NotBlank String nome
) {
}
