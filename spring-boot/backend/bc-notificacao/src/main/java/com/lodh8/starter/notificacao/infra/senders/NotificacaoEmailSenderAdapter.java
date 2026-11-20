package com.lodh8.starter.notificacao.infra.senders;

import com.lodh8.starter.notificacao.app.port.NotificacaoSenderPort;
import com.lodh8.starter.notificacao.domain.Notificacao;
import com.lodh8.starter.notificacao.infra.attributes.NotificacaoAttributes;
import com.lodh8.starter.shared.models.enums.TipoNotificacao;
import com.lodh8.starter.shared.utils.IdentificadorLog;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificacaoEmailSenderAdapter implements NotificacaoSenderPort {

    private final NotificacaoAttributes attributes;

    private final JavaMailSender mailSender;

    @Value("${spring.mail.properties.mail.smtp.from}")
    private String from;

    @Override
    @SneakyThrows
    public void enviar(Notificacao requestEntity) {
        var idMask = IdentificadorLog.mascarar(requestEntity.id());
        log.info("notificacaoEmail enviar inicio id={}", idMask);
        log.debug("notificacaoEmail enviar inicio id={}", requestEntity.id());
        if (attributes.getEmail().isEnabled() && requestEntity.isEmail()) {
            var message = mailSender.createMimeMessage();
            var helper = new MimeMessageHelper(message);
            helper.setFrom(from);
            helper.setTo(requestEntity.destinatarios().toArray(String[]::new));
            helper.setSubject(requestEntity.titulo());
            helper.setText(requestEntity.conteudo(), true);
            mailSender.send(message);
            log.info("notificacaoEmail enviar fim id={} resultado=enviado", idMask);
        } else {
            log.warn("notificacaoEmail enviar id={} resultado=desabilitado_ou_tipo_invalido", idMask);
        }
    }

    @Override
    public boolean suporta(TipoNotificacao tipo) {
        return TipoNotificacao.EMAIL.equals(tipo);
    }

}