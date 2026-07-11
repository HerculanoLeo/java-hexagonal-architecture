package com.lodh8.starter.backoffice.usuarios.infra.attribute;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "api.sistema")
public record SistemaAttributes(
        String redirectUri
) {
}
