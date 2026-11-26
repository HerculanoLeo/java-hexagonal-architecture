package com.lodh8.starter.cadastros.infra.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * DataSource recriado no {@code /actuator/refresh} / FileWatcher (Modelo C).
 * Liquibase continua só no boot — não é {@code @RefreshScope}.
 */
@Configuration
public class RefreshableDataSourceConfiguration {

    @Bean
    @Primary
    @RefreshScope
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    DataSource dataSource(DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }
}
