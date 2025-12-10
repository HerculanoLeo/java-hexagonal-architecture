package com.herculanoleo.starter.notification.app;

import com.herculanoleo.starter.notification.domain.Notificacao;
import com.herculanoleo.starter.notification.domain.NotificacaoRegister;
import com.herculanoleo.starter.notification.domain.NotificacaoSearch;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface NotificacaoService {

    Collection<Notificacao> findAll(NotificacaoSearch requestEntity);

    Optional<Notificacao> findById(UUID id);

    Notificacao register(NotificacaoRegister requestEntity);

    void enviar(UUID id);

    void reenvio(OffsetDateTime dataSolicitacaoDe, OffsetDateTime dataSolicitacaoAte, Integer tentativasMaxima);

}
