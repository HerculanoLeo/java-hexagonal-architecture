package com.lodh8.starter.backoffice.grupos.domain;

import java.util.Collection;

public record GrupoSistemaRegister(
        String nome,
        Collection<String> roles
) {
}
