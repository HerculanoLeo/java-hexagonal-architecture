package com.lodh8.starter.cadastros.infra.configuration;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Modelo C: observa o tmpfs do OpenBao Agent e dispara {@link ContextRefresher}
 * quando {@code application-runtime.properties} (ou certs) mudam — sem reiniciar a JVM.
 */
@Slf4j
@Component
public class OpenBaoRuntimeConfigWatcher {

    static final String PROPERTY_SOURCE_NAME = "openbaoRuntimeProperties";

    private final ContextRefresher contextRefresher;
    private final ConfigurableEnvironment environment;
    private final Path runtimeDir;
    private final Path propertiesFile;
    private final AtomicBoolean running = new AtomicBoolean(false);

    private WatchService watchService;
    private ExecutorService executor;

    public OpenBaoRuntimeConfigWatcher(
            // Sem bootstrap: RefreshAutoConfiguration expõe configDataContextRefresher
            // (legacyContextRefresher só com spring.cloud.bootstrap.enabled=true).
            @Qualifier("configDataContextRefresher") ContextRefresher contextRefresher,
            ConfigurableEnvironment environment,
            @Value("${openbao.runtime-dir:/run/openbao}") String runtimeDir
    ) {
        this.contextRefresher = contextRefresher;
        this.environment = environment;
        this.runtimeDir = Path.of(runtimeDir);
        this.propertiesFile = this.runtimeDir.resolve("config/application-runtime.properties");
    }

    @EventListener(ApplicationReadyEvent.class)
    public void start() {
        if (!Files.isDirectory(runtimeDir)) {
            log.info("openbao watcher ignorado path={} motivo=diretorio-ausente", runtimeDir);
            return;
        }
        if (!running.compareAndSet(false, true)) {
            return;
        }
        try {
            reloadPropertySource();
            watchService = FileSystems.getDefault().newWatchService();
            registerTree(runtimeDir);
            executor = Executors.newSingleThreadExecutor(r -> {
                Thread t = new Thread(r, "openbao-runtime-config-watcher");
                t.setDaemon(true);
                return t;
            });
            executor.submit(this::watchLoop);
            log.info("openbao watcher ativo runtimeDir={}", runtimeDir);
        } catch (IOException ex) {
            running.set(false);
            log.error("openbao watcher falha ao iniciar runtimeDir={}", runtimeDir, ex);
        }
    }

    @PreDestroy
    public void stop() {
        running.set(false);
        if (watchService != null) {
            try {
                watchService.close();
            } catch (IOException ignored) {
                // shutdown
            }
        }
        if (executor != null) {
            executor.shutdownNow();
            try {
                executor.awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void watchLoop() {
        while (running.get()) {
            final WatchKey key;
            try {
                key = watchService.take();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                return;
            } catch (ClosedWatchServiceException ex) {
                return;
            }
            boolean relevant = false;
            for (WatchEvent<?> event : key.pollEvents()) {
                if (event.kind() == StandardWatchEventKinds.OVERFLOW) {
                    relevant = true;
                    continue;
                }
                Path context = (Path) event.context();
                if (context != null) {
                    String name = context.toString();
                    if (name.endsWith(".properties") || name.endsWith(".pfx") || name.endsWith(".pem")
                            || name.endsWith(".crt") || name.endsWith(".key")) {
                        relevant = true;
                    }
                }
            }
            Path dir = (Path) key.watchable();
            key.reset();
            if (relevant) {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    return;
                }
                try {
                    registerTree(runtimeDir);
                    reloadPropertySource();
                    var keys = contextRefresher.refresh();
                    log.info("openbao watcher refresh ok dir={} keysAlteradas={}", dir, keys.size());
                } catch (Exception ex) {
                    log.error("openbao watcher refresh falhou", ex);
                }
            }
        }
    }

    private void registerTree(Path root) throws IOException {
        if (!Files.isDirectory(root)) {
            return;
        }
        try (var paths = Files.walk(root)) {
            paths.filter(Files::isDirectory).forEach(dir -> {
                try {
                    dir.register(
                            watchService,
                            StandardWatchEventKinds.ENTRY_CREATE,
                            StandardWatchEventKinds.ENTRY_MODIFY,
                            StandardWatchEventKinds.ENTRY_DELETE
                    );
                } catch (IOException ex) {
                    log.warn("openbao watcher nao registrou dir={}", dir);
                }
            });
        }
    }

    private void reloadPropertySource() throws IOException {
        if (!Files.isRegularFile(propertiesFile)) {
            log.debug("openbao properties ausente path={}", propertiesFile);
            return;
        }
        Properties props = new Properties();
        try (Reader reader = Files.newBufferedReader(propertiesFile, StandardCharsets.UTF_8)) {
            props.load(reader);
        }
        Map<String, Object> map = new HashMap<>();
        for (String name : props.stringPropertyNames()) {
            map.put(name, props.getProperty(name));
        }
        MutablePropertySources sources = environment.getPropertySources();
        MapPropertySource source = new MapPropertySource(PROPERTY_SOURCE_NAME, map);
        if (sources.contains(PROPERTY_SOURCE_NAME)) {
            sources.replace(PROPERTY_SOURCE_NAME, source);
        } else {
            sources.addFirst(source);
        }
        log.debug("openbao properties carregadas path={} total={}", propertiesFile, map.size());
    }
}
