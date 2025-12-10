package com.herculanoleo.starter.identity.infra.configuration;

import com.herculanoleo.starter.identity.infra.attribute.KeycloakAttributes;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakAdminConfiguration {

    @Bean("keycloakAdminClient")
    Keycloak keycloakAdminClient(final KeycloakAttributes keycloakAttributes) {
        return KeycloakBuilder.builder()
                .serverUrl(keycloakAttributes.url())
                .grantType(keycloakAttributes.granType())
                .clientId(keycloakAttributes.clientId())
                .clientSecret(keycloakAttributes.clientSecret())
                .realm(keycloakAttributes.realm())
                .scope("email profile openid")
                .build();
    }

    @Bean("keycloakAdminRealmResource")
    RealmResource keycloakAdminRealmResource(
            @Qualifier("keycloakAdminClient") final Keycloak keycloakAdminClient,
            final KeycloakAttributes keycloakAttributes) {
        return keycloakAdminClient.realm(keycloakAttributes.realm());
    }

    @Bean("keycloakAdminUsersResource")
    UsersResource keycloakAdminUsersResource(@Qualifier("keycloakAdminRealmResource") final RealmResource realmResource) {
        return realmResource.users();
    }

    @Bean("keycloakAdminGroupResource")
    GroupsResource keycloakAdminGroupResource(@Qualifier("keycloakAdminRealmResource") final RealmResource realmResource) {
        return realmResource.groups();
    }

    @Bean("keycloakAdminRolesResource")
    RolesResource keycloakAdminRolesResource(@Qualifier("keycloakAdminRealmResource") final RealmResource realmResource) {
        return realmResource.roles();
    }

    @Bean("keycloakAdminClientsResource")
    ClientsResource keycloakAdminClientsResource(@Qualifier("keycloakAdminRealmResource") final RealmResource realmResource) {
        return realmResource.clients();
    }

}
