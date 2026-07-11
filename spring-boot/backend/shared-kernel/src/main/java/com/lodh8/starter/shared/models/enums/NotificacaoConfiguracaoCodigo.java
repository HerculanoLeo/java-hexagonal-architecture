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
public enum NotificacaoConfiguracaoCodigo implements MapperEnum {
    BOAS_VINDAS_TITULO("boas_vindas.titulo"),
    TROCA_SENHA_TITULO("troca_senha.titulo"),
    USUARIO_ATIVADO_TITULO("usuario_ativado.titulo"),
    USUARIO_INATIVADO_TITULO("usuario_inativado.titulo"),
    TESTE_TITULO("teste.titulo"),
    DEFAULT_URL_LINK("default_url_link"),
    ;

    private final String value;


}
