package com.herculanoleo.starter.notification.infra.senders;

import com.herculanoleo.starter.notification.app.ports.NotificacaoSenderPort;
import com.herculanoleo.starter.notification.domain.Notificacao;
import com.herculanoleo.starter.notification.infra.attributes.NotificacaoAttributes;
import com.herculanoleo.starter.shared.models.enums.TipoNotificacao;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

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
        if (attributes.getEmail().isEnabled() && requestEntity.isEmail()) {
            var message = mailSender.createMimeMessage();
            var helper = new MimeMessageHelper(message);
            helper.setFrom(from);
            helper.setTo(requestEntity.destinatarios().toArray(String[]::new));
            helper.setSubject(requestEntity.titulo());
            helper.setText(requestEntity.conteudo(), true);
            mailSender.send(message);
        }
    }

    @Override
    public boolean suporta(TipoNotificacao tipo) {
        return TipoNotificacao.EMAIL.equals(tipo);
    }

}
