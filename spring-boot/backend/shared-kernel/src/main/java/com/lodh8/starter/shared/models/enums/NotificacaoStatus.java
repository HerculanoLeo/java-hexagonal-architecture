package com.lodh8.starter.shared.models.enums;

import com.herculanoleo.spring.me.models.annotation.MappedEnum;
import com.herculanoleo.spring.me.models.enums.MapperEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@MappedEnum
public enum NotificacaoStatus implements MapperEnum {
    PENDENTE("P"),
    ENVIADO("E"),
    ERRO("R"),
    ;

    private final String value;

}
