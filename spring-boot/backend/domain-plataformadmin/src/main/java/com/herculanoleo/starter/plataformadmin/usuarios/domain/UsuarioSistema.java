package com.herculanoleo.starter.plataformadmin.usuarios.domain;

import com.herculanoleo.starter.shared.models.enums.Status;

import java.time.OffsetDateTime;
import java.util.UUID;

public record UsuarioSistema(
        UUID id,
        String identityId,
        String email,
        String nome,
        Status status,
        Integer versao,
        OffsetDateTime dataCriacao,
        OffsetDateTime dataAtualizacao
) {
}
