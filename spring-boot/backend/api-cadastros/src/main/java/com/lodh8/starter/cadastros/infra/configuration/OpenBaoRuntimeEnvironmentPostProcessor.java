package com.lodh8.starter.cadastros.infra.configuration;

import org.springframework.boot.EnvironmentPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Importa cedo o YAML do OpenBao Agent (antes de DataSource/Mail bindarem).
 * Complementa {@code spring.config.import} e alimenta o refresh do watcher.
 */
public class OpenBaoRuntimeEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {

    static final String RUNTIME_DIR_PROPERTY = "openbao.runtime-dir";
    static final String DEFAULT_RUNTIME_DIR = "/run/openbao";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String runtimeDir = environment.getProperty(RUNTIME_DIR_PROPERTY, DEFAULT_RUNTIME_DIR);
        Path file = OpenBaoRuntimeProperties.propertiesFile(Path.of(runtimeDir));
        try {
            OpenBaoRuntimeProperties.apply(environment, file);
        } catch (IOException ex) {
            throw new IllegalStateException("Falha ao carregar OpenBao runtime YAML: " + file, ex);
        }
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
