package com.herculanoleo.starter.notification.domain.events;

import com.herculanoleo.starter.shared.models.enums.TipoAcesso;
import com.herculanoleo.starter.shared.models.enums.TipoNotificacao;

public record NotificacaoBoasVindasEvent(
        String nome,
        TipoAcesso tipoAcesso,
        String destinatario,
        String senha,
        TipoNotificacao tipoNotificacao
) {
}
