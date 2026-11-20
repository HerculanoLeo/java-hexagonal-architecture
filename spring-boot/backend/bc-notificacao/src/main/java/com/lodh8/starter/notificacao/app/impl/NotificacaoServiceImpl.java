package com.lodh8.starter.notificacao.app.impl;

import com.lodh8.starter.notificacao.app.NotificacaoService;
import com.lodh8.starter.notificacao.app.port.NotificacaoRepositoryPort;
import com.lodh8.starter.notificacao.app.port.NotificacaoSenderPort;
import com.lodh8.starter.notificacao.domain.Notificacao;
import com.lodh8.starter.notificacao.domain.NotificacaoError;
import com.lodh8.starter.notificacao.domain.NotificacaoRegister;
import com.lodh8.starter.notificacao.domain.NotificacaoSearch;
import com.lodh8.starter.notificacao.domain.events.NotificacaoRegisterEvent;
import com.lodh8.starter.shared.events.app.EventPublisherPort;
import com.lodh8.starter.shared.exceptions.BadRequestException;
import com.lodh8.starter.shared.exceptions.ConflictException;
import com.lodh8.starter.shared.models.enums.NotificacaoStatus;
import com.lodh8.starter.shared.utils.IdentificadorLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificacaoServiceImpl implements NotificacaoService {

    private final NotificacaoRepositoryPort repository;

    private final List<NotificacaoSenderPort> senders;

    private final EventPublisherPort events;

    @Qualifier("applicationTaskExecutor")
    private final AsyncTaskExecutor taskExecutor;

    @Override
    public Collection<Notificacao> findAll(NotificacaoSearch requestEntity) {
        log.info("notificacao findAll inicio");
        log.debug("notificacao findAll inicio search={}", requestEntity);
        var result = this.repository.findAll(requestEntity);
        log.info("notificacao findAll fim total={}", result.size());
        return result;
    }

    @Override
    public Optional<Notificacao> findById(UUID id) {
        log.info("notificacao findById id={}", IdentificadorLog.mascarar(id));
        log.debug("notificacao findById id={}", id);
        var result = this.repository.findById(id);
        if (result.isEmpty()) {
            log.warn("notificacao findById id={} resultado=nao_encontrado", IdentificadorLog.mascarar(id));
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public Notificacao register(NotificacaoRegister requestEntity) {
        log.info("notificacao register inicio tipo={}", requestEntity.tipo());
        log.debug("notificacao register inicio tipo={} destinatarios={}", requestEntity.tipo(), requestEntity.destinatarios());
        var notificacao = this.repository.register(requestEntity);
        events.publishEvent(new NotificacaoRegisterEvent(notificacao));
        log.info("notificacao register fim id={}", IdentificadorLog.mascarar(notificacao.id()));
        log.debug("notificacao register fim id={}", notificacao.id());
        return notificacao;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void enviar(UUID id) {
        log.info("notificacao enviar inicio id={}", IdentificadorLog.mascarar(id));
        log.debug("notificacao enviar inicio id={}", id);
        var notificacaoOpt = this.repository.findById(id);
        if (notificacaoOpt.isEmpty()) {
            log.warn("notificacao enviar id={} resultado=nao_encontrado", IdentificadorLog.mascarar(id));
        }
        var notificacao = notificacaoOpt.orElseThrow();

        if (!NotificacaoStatus.PENDENTE.equals(notificacao.status())) {
            log.warn("notificacao enviar id={} resultado=status_invalido status={}", IdentificadorLog.mascarar(id), notificacao.status());
            throw new BadRequestException("somente notificações pendentes podem ser enviadas");
        }

        enviar(notificacao);
        log.info("notificacao enviar fim id={}", IdentificadorLog.mascarar(id));
        log.debug("notificacao enviar fim id={}", id);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void reenvio(OffsetDateTime dataSolicitacaoDe, OffsetDateTime dataSolicitacaoAte, Integer tentativasMaxima) {
        try {
            log.info("notificacao reenvio inicio tentativasMaxima={}", tentativasMaxima);

            var notificacoes = findAll(new NotificacaoSearch(
                    null,
                    NotificacaoStatus.PENDENTE,
                    dataSolicitacaoDe,
                    dataSolicitacaoAte,
                    1,
                    tentativasMaxima
            ));

            if (!notificacoes.isEmpty()) {
                log.info("notificacao reenvio total={}", notificacoes.size());

                var futures = notificacoes.stream()
                        .map(notificacao -> CompletableFuture.runAsync(() -> enviar(notificacao), taskExecutor))
                        .toArray(CompletableFuture[]::new);
                CompletableFuture.allOf(futures).join();
            } else {
                log.info("notificacao reenvio total=0");
            }
        } catch (Exception ex) {
            log.error("notificacao reenvio erro", ex);
        }
        log.info("notificacao reenvio fim");
    }

    @Transactional(rollbackFor = Throwable.class)
    protected void enviar(Notificacao notificacao) {
        var idMask = IdentificadorLog.mascarar(notificacao.id());
        try {
            var sender = this.senders.stream()
                    .filter(s -> s.suporta(notificacao.tipo()))
                    .findFirst()
                    .orElseThrow(() -> {
                        log.warn("notificacao enviarInterno id={} resultado=tipo_nao_implementado tipo={}", idMask, notificacao.tipo());
                        return new ConflictException("tipo de notificação não implementada");
                    });
            sender.enviar(notificacao);
            this.repository.enviado(notificacao.id());
        } catch (Exception e) {
            log.error("notificacao enviarInterno id={} falha", idMask, e);
            this.repository.erro(notificacao.id(), notificacao.tentativas() + 1, new NotificacaoError(e.getMessage(), OffsetDateTime.now()));
        }
    }

}