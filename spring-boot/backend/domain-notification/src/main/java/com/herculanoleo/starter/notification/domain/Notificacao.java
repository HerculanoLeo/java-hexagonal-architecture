package com.herculanoleo.starter.notification.domain;

import com.herculanoleo.starter.shared.models.enums.NotificacaoStatus;
import com.herculanoleo.starter.shared.models.enums.TipoNotificacao;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record Notificacao(
        UUID id,
        String titulo,
        String conteudo,
        Collection<String> destinatarios,
        OffsetDateTime dataSolicitacao,
        OffsetDateTime dataEnvio,
        NotificacaoStatus status,
        TipoNotificacao tipo,
        Integer tentativas,
        Integer versao,
        OffsetDateTime dataCriacao,
        OffsetDateTime dataAtualizacao,
        Map<String, Object> metadados,
        List<NotificacaoError> errors
) {

    public boolean isEmail() {
        return TipoNotificacao.EMAIL.equals(tipo);
    }

    public boolean isSMS() {
        return TipoNotificacao.SMS.equals(tipo);
    }

}
