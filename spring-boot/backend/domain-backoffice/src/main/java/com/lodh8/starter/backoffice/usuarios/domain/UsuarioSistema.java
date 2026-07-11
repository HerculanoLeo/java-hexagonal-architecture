package com.lodh8.starter.backoffice.usuarios.domain;

import com.lodh8.starter.shared.models.enums.Status;

import java.time.OffsetDateTime;
import java.util.UUID;

public record UsuarioSistema(
        UUID id,
        String identityId,
        boolean main,
        String email,
        String nome,
        Status status,
        Integer versao,
        OffsetDateTime dataCriacao,
        OffsetDateTime dataAtualizacao
) {
}
