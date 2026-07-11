package com.lodh8.starter.shared.utils;

import com.lodh8.starter.shared.models.enums.TipoAcesso;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface AttributesMapper {

    String relacionadoId();

    TipoAcesso tipo();

    default Map<String, List<String>> attributes() {
        var attributes = new HashMap<String, List<String>>();
        attributes.put(TipoAcesso.APPLICATION_TYPE_KEY, List.of(tipo().getValue()));
        if (StringUtils.isNotBlank(relacionadoId())) {
            attributes.put(TipoAcesso.APPLICATION_RELACIONADO_ID_KEY, List.of(relacionadoId()));
        }
        return attributes;
    }

}
