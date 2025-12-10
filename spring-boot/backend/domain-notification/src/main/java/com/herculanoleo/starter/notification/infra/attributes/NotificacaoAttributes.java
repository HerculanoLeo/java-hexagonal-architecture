package com.herculanoleo.starter.notification.infra.attributes;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("api.notificacao")
public class NotificacaoAttributes {

    private String cronReenvio = "0 */15 * * * *";

    private Email email = new Email();

    @Data
    public static final class Email {
        private boolean enabled = false;
        private Integer maxTentativas = 5;
    }
}
