package com.lodh8.starter.notificacao.app.port;

import com.lodh8.starter.notificacao.domain.Notificacao;
import com.lodh8.starter.notificacao.domain.NotificacaoError;
import com.lodh8.starter.notificacao.domain.NotificacaoRegister;
import com.lodh8.starter.notificacao.domain.NotificacaoSearch;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface NotificacaoRepositoryPort {

    Collection<Notificacao> findAll(NotificacaoSearch requestEntity);

    Optional<Notificacao> findById(UUID id);

    Notificacao register(NotificacaoRegister requestEntity);

    void enviado(UUID id);

    void erro(UUID id, Integer tentativa, NotificacaoError error);

}
