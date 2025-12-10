package com.herculanoleo.starter.plataformadmin.grupos.domain;

import java.util.Collection;

public record GrupoSistema(
        String id,
        String nome,
        Collection<String> roles
) {
}
