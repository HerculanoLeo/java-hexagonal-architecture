package com.herculanoleo.starter.notification.domain;

import com.herculanoleo.starter.shared.models.enums.TipoNotificacao;

import java.util.Collection;
import java.util.Map;

public record NotificacaoRegister(
        String titulo,
        String conteudo,
        Collection<String> destinatarios,
        TipoNotificacao tipo,
        Map<String, Object> metadados
) {
}
