package com.herculanoleo.starter.authorize.domain;

import java.util.Collection;

public record GrupoAutenticado(
        String id,
        String nome,
        Collection<String> roles
) {
}
