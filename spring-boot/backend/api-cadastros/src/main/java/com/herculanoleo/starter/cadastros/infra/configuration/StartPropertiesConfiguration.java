package com.herculanoleo.starter.cadastros.infra.configuration;

import com.herculanoleo.starter.identity.infra.attribute.KeycloakAttributes;
import com.herculanoleo.starter.plataformadmin.usuarios.infra.attribute.SistemaAttributes;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({KeycloakAttributes.class, SistemaAttributes.class})
public class StartPropertiesConfiguration {
}
