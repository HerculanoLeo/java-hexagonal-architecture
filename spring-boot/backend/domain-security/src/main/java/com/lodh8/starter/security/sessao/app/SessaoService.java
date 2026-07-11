package com.lodh8.starter.security.sessao.app;

public interface SessaoService {

    /**
     * Invalida todas as sessões Keycloak do usuário identificado pelo id no IdP.
     * Agnóstico a tipo de acesso / tenant.
     */
    void invalidateByIdentityId(String identityId);

}
