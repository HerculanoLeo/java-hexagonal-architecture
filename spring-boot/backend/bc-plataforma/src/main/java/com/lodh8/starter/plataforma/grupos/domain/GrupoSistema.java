package com.lodh8.starter.plataforma.grupos.domain;

import java.util.Collection;

public record GrupoSistema(
        String id,
        String nome,
        Collection<String> roles
) {
}
