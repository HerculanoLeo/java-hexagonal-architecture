package com.lodh8.starter.identity.domain.attributes;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "api.oauth.keycloak")
public class KeycloakAttributes {

    private String url;
    private String realm;
    private String[] roleMapKeys;
    private String clientIdUsers;
    private String clientId;
    private String clientSecret;
    private String granType;
}
