package com.lodh8.starter.identity.infra.attribute;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "api.oauth.keycloak")
public record KeycloakAttributes(
    String url,
    String realm,
    String[] roleMapKeys,
    String clientIdUsers,
    String clientId,
    String clientSecret,
    String granType
) {
}
