package com.herculanoleo.starter.cadastros.infra.attributes;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.util.Collections;
import java.util.List;

@Data
@Configuration
@ConfigurationProperties("api.web")
public class WebAttributes {
    private Cors cors = new Cors();

    @Data
    public static class Cors {
        private List<String> allowedOrigins = Collections.emptyList();

        private List<String> allowedMethods = List.of(
                HttpMethod.GET.name(),
                HttpMethod.POST.name(),
                HttpMethod.PUT.name(),
                HttpMethod.DELETE.name(),
                HttpMethod.PATCH.name(),
                HttpMethod.HEAD.name(),
                HttpMethod.OPTIONS.name()
        );

        private List<String> allowedHeaders = List.of(
                HttpHeaders.AUTHORIZATION,
                HttpHeaders.CONTENT_TYPE,
                "X-Requested-With"
        );

        private List<String> exposedHeaders = List.of(
                HttpHeaders.CONTENT_DISPOSITION,
                HttpHeaders.ETAG,
                HttpHeaders.LINK,
                HttpHeaders.RETRY_AFTER
        );
    }
}
