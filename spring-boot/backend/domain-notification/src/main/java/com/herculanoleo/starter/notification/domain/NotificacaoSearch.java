package com.herculanoleo.starter.notification.domain;

import com.herculanoleo.starter.shared.models.enums.NotificacaoStatus;
import com.herculanoleo.starter.shared.models.enums.TipoNotificacao;

import java.time.OffsetDateTime;

public record NotificacaoSearch(
        TipoNotificacao tipo,
        NotificacaoStatus status,
        OffsetDateTime dataSolicitacaoDe,
        OffsetDateTime dataSolicitacaoAte,
        Integer minTentativas,
        Integer maxTentativas
) {
}
