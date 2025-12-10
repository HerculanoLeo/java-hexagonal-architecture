package com.herculanoleo.starter.shared.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FreemarkerTemplate {
    BOAS_VINDAS("boas-vindas.ftlh"),
    TROCA_SENHA("troca-senha.ftlh"),
    ;

    private final String value;

}
