package com.lodh8.starter.identity.grupos.domain;

import com.lodh8.starter.shared.models.enums.TipoAcesso;

import java.util.Collection;

public record Grupo(
        String id,
        String relacionadoId,
        String nome,
        Collection<String> roles,
        TipoAcesso tipo
) {
    public String nomeInterno() {
        if (null == relacionadoId) {
            return nome;
        }
        return "%s:ID_%s".formatted(nome, relacionadoId);
    }
}
