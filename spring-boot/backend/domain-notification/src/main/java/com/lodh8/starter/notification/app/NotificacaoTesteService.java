package com.lodh8.starter.notification.app;

import com.lodh8.starter.notification.domain.NotificacaoTesteTipo;

public interface NotificacaoTesteService {

    void enviarEmailTeste();

    void enviarEmailTeste(NotificacaoTesteTipo tipo);

}
