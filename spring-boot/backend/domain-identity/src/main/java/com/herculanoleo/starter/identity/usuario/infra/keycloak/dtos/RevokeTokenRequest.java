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
public class RevokeTokenRequest {

    @FormProperty("client_id")
    private String clientId;

    @FormProperty("token")
    private String token;

    @FormProperty("token_type_hint")
    private String tokenTypeHint;

}
