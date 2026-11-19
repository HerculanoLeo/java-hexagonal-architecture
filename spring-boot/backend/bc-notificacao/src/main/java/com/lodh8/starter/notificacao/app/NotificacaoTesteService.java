package com.lodh8.starter.notificacao.app;

import com.lodh8.starter.notificacao.domain.NotificacaoTesteTipo;

public interface NotificacaoTesteService {

    void enviarEmailTeste();

    void enviarEmailTeste(NotificacaoTesteTipo tipo);

}
