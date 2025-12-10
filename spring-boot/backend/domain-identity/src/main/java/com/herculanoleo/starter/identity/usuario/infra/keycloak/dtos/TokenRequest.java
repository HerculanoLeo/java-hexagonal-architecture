package com.herculanoleo.starter.identity.usuario.infra.keycloak.dtos;

import feign.form.FormProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenRequest {

    @FormProperty("client_id")
    private String clientId;

    @FormProperty("username")
    private String username;

    @FormProperty("password")
    private String password;

    @FormProperty("client_secret")
    private String clientSecret;

    @FormProperty("grant_type")
    private String grantType;

    @FormProperty("scope")
    private String scope;

}
