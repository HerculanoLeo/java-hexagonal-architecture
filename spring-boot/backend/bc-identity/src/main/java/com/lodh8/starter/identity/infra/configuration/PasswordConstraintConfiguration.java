package com.lodh8.starter.identity.infra.configuration;

import com.lodh8.starter.identity.infra.attribute.PasswordConstraintAttributes;
import org.passay.PasswordValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PasswordConstraintConfiguration {
    @Bean
    public PasswordValidator passwordValidator(final PasswordConstraintAttributes attributes) {
        return new PasswordValidator(attributes.generateRules());
    }
}
