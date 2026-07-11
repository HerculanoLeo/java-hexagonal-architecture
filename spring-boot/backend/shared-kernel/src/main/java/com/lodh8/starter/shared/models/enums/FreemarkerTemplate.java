package com.lodh8.starter.shared.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FreemarkerTemplate {
    BOAS_VINDAS("mail/notificacao/boas-vindas.ftlh"),
    TROCA_SENHA("mail/notificacao/troca-senha.ftlh"),
    USUARIO_ATIVADO("mail/notificacao/usuario-ativado.ftlh"),
    USUARIO_INATIVADO("mail/notificacao/usuario-inativado.ftlh"),
    TESTE("mail/notificacao/teste/index.ftlh"),
    ;

    private final String value;

}
