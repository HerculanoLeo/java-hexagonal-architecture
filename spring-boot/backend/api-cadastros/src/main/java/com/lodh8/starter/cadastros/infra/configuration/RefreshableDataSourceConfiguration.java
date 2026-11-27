package com.lodh8.starter.cadastros.infra.configuration;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;
import org.springframework.boot.liquibase.autoconfigure.LiquibaseDataSource;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

/**
 * DataSource estável (sem {@code @RefreshScope}): o pool da app só troca quando
 * {@code spring.datasource.*} muda. Liquibase usa um Hikari dedicado no boot
 * ({@link LiquibaseDataSource}) — o proxy não expõe {@code url} por reflexão.
 */
@Slf4j
@Configuration
public class RefreshableDataSourceConfiguration {

    private SwappingDataSource swappingDataSource;

    @Bean
    @Primary
    DataSource dataSource(DataSourceProperties properties, Environment environment) {
        HikariDataSource hikari = buildHikari(properties, environment);
        this.swappingDataSource = new SwappingDataSource(hikari);
        return this.swappingDataSource;
    }

    /**
     * Migrações só no boot; precisa de Hikari “real” para o
     * {@code DataSourceBuilder.derivedFrom(...)} do auto-config Liquibase.
     */
    @Bean
    @LiquibaseDataSource
    DataSource liquibaseDataSource(DataSourceProperties properties, Environment environment) {
        return buildHikari(properties, environment);
    }

    @EventListener
    void onEnvironmentChange(EnvironmentChangeEvent event, Environment environment) {
        boolean datasourceChanged = event.getKeys().stream()
                .anyMatch(key -> key.startsWith("spring.datasource"));
        if (!datasourceChanged || swappingDataSource == null) {
            return;
        }
        DataSourceProperties properties = new DataSourceProperties();
        Binder.get(environment).bind("spring.datasource", Bindable.ofInstance(properties));
        swappingDataSource.swap(buildHikari(properties, environment));
        log.info("openbao datasource pool trocado motivo=spring.datasource");
    }

    private static HikariDataSource buildHikari(DataSourceProperties properties, Environment environment) {
        HikariDataSource hikari = properties.initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
        Binder.get(environment).bind("spring.datasource.hikari", Bindable.ofInstance(hikari));
        return hikari;
    }
}
