package com.herculanoleo.starter.plataformadmin.grupos.domain;

import java.util.Collection;

public record GrupoSistemaRegister(
        String nome,
        Collection<String> roles
) {
}
