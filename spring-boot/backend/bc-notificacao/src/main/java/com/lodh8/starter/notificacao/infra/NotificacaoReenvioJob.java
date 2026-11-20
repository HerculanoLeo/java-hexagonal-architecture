package com.lodh8.starter.notificacao.infra;

import com.lodh8.starter.notificacao.app.NotificacaoService;
import com.lodh8.starter.notificacao.infra.attributes.NotificacaoAttributes;
import com.lodh8.starter.shared.utils.LogMdc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificacaoReenvioJob {

    private final NotificacaoAttributes attributes;

    private final NotificacaoService service;

    @Scheduled(cron = "${api.notificacao.cronReenvio:0 */15 * * * *}")
    public synchronized void run() {
        LogMdc.putUid(UUID.randomUUID().toString());
        try {
            var dataDe = OffsetDateTime.now().minusDays(1);
            var dataAte = OffsetDateTime.now().minusHours(1);
            var maxTentativas = attributes.getEmail().getMaxTentativas();
            log.info("notificacaoReenvioJob run inicio tentativasMaxima={}", maxTentativas);
            this.service.reenvio(dataDe, dataAte, maxTentativas);
            log.info("notificacaoReenvioJob run fim");
        } finally {
            LogMdc.clear();
        }
    }

}
