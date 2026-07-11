package com.lodh8.starter.cadastros.infra.configuration;

import com.lodh8.starter.backoffice.usuarios.infra.attribute.SistemaAttributes;
import com.lodh8.starter.identity.infra.attribute.KeycloakAttributes;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({KeycloakAttributes.class, SistemaAttributes.class})
public class StartPropertiesConfiguration {
}
