package com.herculanoleo.starter.identity.grupos.domain;

import com.herculanoleo.starter.shared.models.enums.TipoAcesso;
import com.herculanoleo.starter.shared.utils.AttributesMapper;

import java.util.Collection;

public record GrupoRegister(
        String relacionadoId,
        TipoAcesso tipo,
        String nome,
        Collection<String> roles
) implements AttributesMapper {
    public String nomeInterno() {
        if (null == relacionadoId) {
            return nome;
        }
        return "%s:ID_%s".formatted(nome, relacionadoId);
    }

}
