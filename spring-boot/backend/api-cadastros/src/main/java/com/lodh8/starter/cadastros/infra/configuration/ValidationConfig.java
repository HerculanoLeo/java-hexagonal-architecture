package com.lodh8.starter.cadastros.infra.configuration;

import com.herculanoleo.sentinelflow.validations.ValidationFactory;
import com.herculanoleo.sentinelflow.validations.impl.ValidationFactoryImpl;
import com.herculanoleo.sentinelflow.validator.ValidatorFactory;
import com.herculanoleo.sentinelflow.validator.impl.ValidatorFactoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidationConfig {

    @Bean
    public ValidationFactory validationFactory() {
        return new ValidationFactoryImpl();
    }

    @Bean
    public ValidatorFactory validatorFactory() {
        return new ValidatorFactoryImpl();
    }

}
