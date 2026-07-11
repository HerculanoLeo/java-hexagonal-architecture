package com.lodh8.starter.shared.models.enums;

import com.herculanoleo.spring.me.models.annotation.MapperEnumDBConverter;
import com.herculanoleo.spring.me.models.annotation.MapperEnumType;
import com.herculanoleo.spring.me.models.enums.MapperEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@MapperEnumType
@MapperEnumDBConverter
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
