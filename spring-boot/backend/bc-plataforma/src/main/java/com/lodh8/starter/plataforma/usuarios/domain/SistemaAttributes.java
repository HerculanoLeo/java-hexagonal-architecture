package com.lodh8.starter.plataforma.usuarios.domain;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "api.sistema")
public class SistemaAttributes {

    private String redirectUri;
}
