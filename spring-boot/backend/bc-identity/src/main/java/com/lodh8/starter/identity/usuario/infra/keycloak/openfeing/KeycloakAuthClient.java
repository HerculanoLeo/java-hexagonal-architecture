package com.lodh8.starter.identity.usuario.infra.keycloak.openfeing;

import com.lodh8.starter.identity.usuario.infra.keycloak.dtos.LogoutRequest;
import com.lodh8.starter.identity.usuario.infra.keycloak.dtos.OAuthTokenResponse;
import com.lodh8.starter.identity.usuario.infra.keycloak.dtos.RevokeTokenRequest;
import com.lodh8.starter.identity.usuario.infra.keycloak.dtos.TokenRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "keycloak-auth-client",
        url = "${api.oauth.keycloak.url}/realms/${api.oauth.keycloak.realm}/protocol/openid-connect")
public interface KeycloakAuthClient {

    @PostMapping(value = "token", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    OAuthTokenResponse token(@RequestBody TokenRequest requestEntity);

    @PostMapping(value = "revoke", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    ResponseEntity<Void> revoke(@RequestBody RevokeTokenRequest requestEntity);

    @PostMapping(value = "logout", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    ResponseEntity<Void> logout(@RequestBody LogoutRequest requestEntity);

}
