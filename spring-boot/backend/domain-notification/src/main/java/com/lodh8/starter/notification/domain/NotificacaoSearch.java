package com.lodh8.starter.notification.domain;

import com.lodh8.starter.shared.models.enums.NotificacaoStatus;
import com.lodh8.starter.shared.models.enums.TipoNotificacao;

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
