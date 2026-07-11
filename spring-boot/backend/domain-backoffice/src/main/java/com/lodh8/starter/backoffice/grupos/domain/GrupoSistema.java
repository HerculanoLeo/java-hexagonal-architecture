package com.lodh8.starter.backoffice.grupos.domain;

import java.util.Collection;

public record GrupoSistema(
        String id,
        String nome,
        Collection<String> roles
) {
}
