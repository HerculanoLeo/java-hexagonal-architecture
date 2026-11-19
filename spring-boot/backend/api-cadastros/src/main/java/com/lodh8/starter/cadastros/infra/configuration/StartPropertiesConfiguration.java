package com.lodh8.starter.cadastros.infra.configuration;

import com.lodh8.starter.identity.domain.attributes.KeycloakAttributes;
import com.lodh8.starter.plataforma.usuarios.domain.SistemaAttributes;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({KeycloakAttributes.class, SistemaAttributes.class})
public class StartPropertiesConfiguration {
}
