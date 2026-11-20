package com.lodh8.starter.notificacao.app.port;

import com.lodh8.starter.notificacao.domain.Notificacao;
import com.lodh8.starter.shared.models.enums.TipoNotificacao;

public interface NotificacaoSenderPort {

    void enviar(Notificacao requestEntity);

    boolean suporta(TipoNotificacao tipo);

}
