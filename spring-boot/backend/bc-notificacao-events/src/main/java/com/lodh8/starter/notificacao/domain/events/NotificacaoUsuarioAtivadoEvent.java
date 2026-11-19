package com.lodh8.starter.notificacao.domain.events;

import com.lodh8.starter.shared.models.enums.TipoAcesso;
import com.lodh8.starter.shared.models.enums.TipoNotificacao;

public record NotificacaoUsuarioAtivadoEvent(
        String nome,
        TipoAcesso tipoAcesso,
        String destinatario,
        TipoNotificacao tipoNotificacao
) {
}
