package com.lodh8.starter.identity.clientapi.domain;

import com.lodh8.starter.shared.models.enums.TipoAcesso;

import java.util.List;
import java.util.Map;

public record ClientApiRegister(
        String clientId,
        String nome,
        TipoAcesso tipo,
        boolean enabled
) {
    public Map<String, List<String>> attributes() {
        return Map.of(
                TipoAcesso.APPLICATION_TYPE_KEY, List.of(tipo.getValue())
        );
    }
}
