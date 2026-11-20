package com.lodh8.starter.identity.roles.infra.keycloak.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum KeycloakDefaultRealmRole {
    UMA_AUTHORIZATION("uma_authorization"),
    OFFLINE_ACCESS("offline_access"),
    DEFAULT_ROLES("default-roles-"),
    ;
    private final String value;

}
