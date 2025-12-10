package com.herculanoleo.starter.notification.app.impl;

import com.herculanoleo.starter.notification.app.NotificacaoService;
import com.herculanoleo.starter.notification.app.ports.NotificacaoRepositoryPort;
import com.herculanoleo.starter.notification.app.ports.NotificacaoSenderPort;
import com.herculanoleo.starter.notification.domain.Notificacao;
import com.herculanoleo.starter.notification.domain.NotificacaoError;
import com.herculanoleo.starter.notification.domain.NotificacaoRegister;
import com.herculanoleo.starter.notification.domain.NotificacaoSearch;
import com.herculanoleo.starter.notification.domain.events.NotificacaoRegisterEvent;
import com.herculanoleo.starter.shared.events.app.EventPublisherPort;
import com.herculanoleo.starter.shared.exceptions.BadRequestException;
import com.herculanoleo.starter.shared.exceptions.ConflictException;
import com.herculanoleo.starter.shared.models.enums.NotificacaoStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Executors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificacaoServiceImpl implements NotificacaoService {

    private final NotificacaoRepositoryPort repository;

    private final List<NotificacaoSenderPort> senders;

    private final EventPublisherPort events;

    @Override
    public Collection<Notificacao> findAll(NotificacaoSearch requestEntity) {
        return this.repository.findAll(requestEntity);
    }

    @Override
    public Optional<Notificacao> findById(UUID id) {
        return this.repository.findById(id);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public Notificacao register(NotificacaoRegister requestEntity) {
        var notificacao = this.repository.register(requestEntity);
        events.publishEvent(new NotificacaoRegisterEvent(notificacao));
        return notificacao;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void enviar(UUID id) {
        var notificacao = this.repository.findById(id).orElseThrow();

        if (!NotificacaoStatus.PENDENTE.equals(notificacao.status())) {
            throw new BadRequestException("somente notificações pendentes podem ser enviadas");
        }

        enviar(notificacao);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void reenvio(OffsetDateTime dataSolicitacaoDe, OffsetDateTime dataSolicitacaoAte, Integer tentativasMaxima) {
        try {
            log.info("iniciando processamento das notificacoes");

            var notificacoes = findAll(new NotificacaoSearch(
                    null,
                    NotificacaoStatus.PENDENTE,
                    dataSolicitacaoDe,
                    dataSolicitacaoAte,
                    1,
                    tentativasMaxima
            ));

            if (!notificacoes.isEmpty()) {
                log.info("ha {} notificacoes para serem processadas.", notificacoes.size());

                try (var executor = Executors.newThreadPerTaskExecutor(Thread.ofVirtual().name("reenvio-notificacoes").factory())) {
                    for (final var notificacao : notificacoes) {
                        executor.execute(() -> enviar(notificacao));
                    }
                }
            } else {
                log.info("nenhuma notificacao encontrada para processamento");
            }
        } catch (Exception ex) {
            log.error("erro ao processar as notificacoes", ex);
        }
        log.info("finalizado o processamento das notificacoes");
    }

    @Transactional(rollbackFor = Throwable.class)
    protected void enviar(Notificacao notificacao) {
        try {
            var sender = this.senders.stream()
                    .filter(s -> s.suporta(notificacao.tipo()))
                    .findFirst()
                    .orElseThrow(() -> new ConflictException("tipo de notificação não implementada"));
            sender.enviar(notificacao);
            this.repository.enviado(notificacao.id());
        } catch (Exception e) {
            log.error("falha ao enviar a notificacao de id {}", notificacao.id(), e);
            this.repository.erro(notificacao.id(), notificacao.tentativas() + 1, new NotificacaoError(e.getMessage(), OffsetDateTime.now()));
        }
    }

}
