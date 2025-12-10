package com.herculanoleo.starter.identity.grupos.domain;

import java.util.Collection;

public record GrupoUpdate(
        String nome,
        Collection<String> roles
) {

    public GrupoUpdate setNomeInterno(String nomeInterno) {
        return new GrupoUpdate(nomeInterno, roles);
    }

}
