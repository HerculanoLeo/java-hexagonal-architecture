package com.lodh8.starter.notification.domain.events;

import com.lodh8.starter.shared.models.enums.TipoAcesso;
import com.lodh8.starter.shared.models.enums.TipoNotificacao;

public record NotificacaoBoasVindasEvent(
        String nome,
        TipoAcesso tipoAcesso,
        String destinatario,
        String senha,
        TipoNotificacao tipoNotificacao
) {
}
