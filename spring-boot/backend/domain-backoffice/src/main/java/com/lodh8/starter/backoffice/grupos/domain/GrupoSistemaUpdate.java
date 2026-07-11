package com.lodh8.starter.backoffice.grupos.domain;

import java.util.Collection;

public record GrupoSistemaUpdate(
        String nome,
        Collection<String> roles
) {
}
