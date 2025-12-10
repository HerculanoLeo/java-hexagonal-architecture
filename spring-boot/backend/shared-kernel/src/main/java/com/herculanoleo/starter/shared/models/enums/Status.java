package com.herculanoleo.starter.shared.models.enums;

import com.herculanoleo.spring.me.models.annotation.MapperEnumDBConverter;
import com.herculanoleo.spring.me.models.enums.MapperEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@MapperEnumDBConverter
public enum Status implements MapperEnum {
    ATIVO("A"), INATIVO("I"),
    ;

    private final String value;

}
