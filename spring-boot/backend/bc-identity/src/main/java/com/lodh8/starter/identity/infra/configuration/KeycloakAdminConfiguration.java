package com.lodh8.starter.identity.infra.configuration;

import com.lodh8.starter.identity.domain.attributes.KeycloakAttributes;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakAdminConfiguration {

    @Bean("keycloakAdminClient")
    @RefreshScope
    Keycloak keycloakAdminClient(final KeycloakAttributes keycloakAttributes) {
        return KeycloakBuilder.builder()
                .serverUrl(keycloakAttributes.getUrl())
                .grantType(keycloakAttributes.getGranType())
                .clientId(keycloakAttributes.getClientId())
                .clientSecret(keycloakAttributes.getClientSecret())
                .realm(keycloakAttributes.getRealm())
                .build();
    }

    @Bean("keycloakAdminRealmResource")
    @RefreshScope
    RealmResource keycloakAdminRealmResource(
            @Qualifier("keycloakAdminClient") final Keycloak keycloakAdminClient,
            final KeycloakAttributes keycloakAttributes) {
        return keycloakAdminClient.realm(keycloakAttributes.getRealm());
    }

    @Bean("keycloakAdminUsersResource")
    @RefreshScope
    UsersResource keycloakAdminUsersResource(@Qualifier("keycloakAdminRealmResource") final RealmResource realmResource) {
        return realmResource.users();
    }

    @Bean("keycloakAdminGroupResource")
    @RefreshScope
    GroupsResource keycloakAdminGroupResource(@Qualifier("keycloakAdminRealmResource") final RealmResource realmResource) {
        return realmResource.groups();
    }

    @Bean("keycloakAdminRolesResource")
    @RefreshScope
    RolesResource keycloakAdminRolesResource(@Qualifier("keycloakAdminRealmResource") final RealmResource realmResource) {
        return realmResource.roles();
    }

    @Bean("keycloakAdminClientsResource")
    @RefreshScope
    ClientsResource keycloakAdminClientsResource(@Qualifier("keycloakAdminRealmResource") final RealmResource realmResource) {
        return realmResource.clients();
    }

}
