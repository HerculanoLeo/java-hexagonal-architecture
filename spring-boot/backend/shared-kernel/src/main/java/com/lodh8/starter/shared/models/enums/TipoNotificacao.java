package com.lodh8.starter.shared.models.enums;

import com.herculanoleo.spring.me.models.annotation.MappedEnum;
import com.herculanoleo.spring.me.models.enums.MapperEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@MappedEnum
public enum TipoNotificacao implements MapperEnum {
    // somente email será implementado nessa versao, os outros tipos estão para exemplo de demonstracao da abstracao de
    // notificacao
    EMAIL("EM"),
    SMS("SM"),
    PUSH("PU"),
    WHATSAPP("WH"),
    ;

    private final String value;


}
