package com.herculanoleo.starter.plataformadmin.grupos.domain;

import java.util.Collection;

public record GrupoSistemaUpdate(
        String nome,
        Collection<String> roles
) {
}
