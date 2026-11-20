package com.lodh8.starter.plataforma.usuarios.domain;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "api.sistema")
public record SistemaAttributes(
        String redirectUri
) {
}
