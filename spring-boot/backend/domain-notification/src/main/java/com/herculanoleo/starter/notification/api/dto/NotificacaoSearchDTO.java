package com.herculanoleo.starter.notification.api.dto;

import com.herculanoleo.starter.shared.models.enums.NotificacaoStatus;
import com.herculanoleo.starter.shared.models.enums.TipoNotificacao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificacaoSearchDTO {

    private TipoNotificacao tipo;

    private NotificacaoStatus status;

    private OffsetDateTime dataSolicitacaoDe;

    private OffsetDateTime dataSolicitacaoAte;

    private Integer minTentativas;

    private Integer maxTentativas;

}
