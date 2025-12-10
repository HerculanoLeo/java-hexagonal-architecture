package com.herculanoleo.starter.shared.models.enums;

import com.herculanoleo.spring.me.models.annotation.MapperEnumDBConverter;
import com.herculanoleo.spring.me.models.enums.MapperEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@MapperEnumDBConverter
public enum TipoAcesso implements MapperEnum {
    USUARIO_SISTEMA("ST", "admin-sistemas"),
    CLIENTE_SISTEMA("CL", "admin-sistemas"),
    ANONYMOUS("AN", null),
    ;

    private final String value;

    private final String adminRole;

    public static final String APPLICATION_TYPE_KEY = "application.type";

    public static final String APPLICATION_RELACIONADO_ID_KEY = "application.relacionadoId";

}
