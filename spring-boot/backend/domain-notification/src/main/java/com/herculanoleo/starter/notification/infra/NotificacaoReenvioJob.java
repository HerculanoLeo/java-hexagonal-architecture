package com.herculanoleo.starter.notification.infra;

import com.herculanoleo.starter.notification.app.NotificacaoService;
import com.herculanoleo.starter.notification.infra.attributes.NotificacaoAttributes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificacaoReenvioJob {

    private final NotificacaoAttributes attributes;

    private final NotificacaoService service;

    @Scheduled(cron = "${api.notificacao.cronReenvio:0 */15 * * * *}")
    public synchronized void run() {
        var dataDe = OffsetDateTime.now().minusDays(1);
        var dataAte = OffsetDateTime.now().minusHours(1);
        var maxTentativas = attributes.getEmail().getMaxTentativas();
        this.service.reenvio(dataDe, dataAte, maxTentativas);
    }

}
