package com.lodh8.starter.cadastros.infra.configuration;

import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.FileSystemResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Carrega {@code application-runtime.yml} do Agent (KV flat → YAML Spring)
 * como property source com keys dotted.
 */
final class OpenBaoRuntimeProperties {

    static final String PROPERTY_SOURCE_NAME = "openbaoRuntimeYaml";

    private static final YamlPropertySourceLoader YAML_LOADER = new YamlPropertySourceLoader();

    enum ApplyResult {
        MISSING,
        UNCHANGED,
        CHANGED
    }

    private OpenBaoRuntimeProperties() {
    }

    static Path propertiesFile(Path runtimeDir) {
        return runtimeDir.resolve("config/application-runtime.yml");
    }

    static ApplyResult apply(ConfigurableEnvironment environment, Path yamlFile) throws IOException {
        if (!Files.isRegularFile(yamlFile)) {
            return ApplyResult.MISSING;
        }
        List<PropertySource<?>> loaded = YAML_LOADER.load(PROPERTY_SOURCE_NAME, new FileSystemResource(yamlFile));
        if (loaded.isEmpty()) {
            return ApplyResult.MISSING;
        }
        Map<String, Object> merged = new HashMap<>();
        for (PropertySource<?> source : loaded) {
            if (source instanceof OriginTrackedMapPropertySource mapSource) {
                for (String name : mapSource.getPropertyNames()) {
                    merged.put(name, mapSource.getProperty(name));
                }
            } else if (source.getSource() instanceof Map<?, ?> map) {
                map.forEach((k, v) -> merged.put(String.valueOf(k), v));
            }
        }
        MutablePropertySources sources = environment.getPropertySources();
        PropertySource<?> existing = sources.get(PROPERTY_SOURCE_NAME);
        if (existing != null && existing.getSource() instanceof Map<?, ?> oldMap
                && mapsEqual(oldMap, merged)) {
            return ApplyResult.UNCHANGED;
        }
        PropertySource<?> source = new OriginTrackedMapPropertySource(PROPERTY_SOURCE_NAME, merged, true);
        if (sources.contains(PROPERTY_SOURCE_NAME)) {
            sources.replace(PROPERTY_SOURCE_NAME, source);
        } else {
            sources.addFirst(source);
        }
        return ApplyResult.CHANGED;
    }

    private static boolean mapsEqual(Map<?, ?> left, Map<String, Object> right) {
        if (left.size() != right.size()) {
            return false;
        }
        for (Map.Entry<String, Object> entry : right.entrySet()) {
            if (!left.containsKey(entry.getKey())) {
                return false;
            }
            if (!Objects.equals(stringify(left.get(entry.getKey())), stringify(entry.getValue()))) {
                return false;
            }
        }
        return true;
    }

    private static String stringify(Object value) {
        return value == null ? null : String.valueOf(value);
    }
}
