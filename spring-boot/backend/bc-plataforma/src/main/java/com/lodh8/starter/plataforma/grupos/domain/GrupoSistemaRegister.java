package com.lodh8.starter.plataforma.grupos.domain;

import java.util.Collection;

public record GrupoSistemaRegister(
        String nome,
        Collection<String> roles
) {
}
