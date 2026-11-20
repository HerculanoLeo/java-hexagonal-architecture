package com.lodh8.starter.authorize.app;

import com.lodh8.starter.authorize.domain.GrupoAutenticado;
import com.lodh8.starter.authorize.domain.OAuthUser;
import com.lodh8.starter.authorize.domain.UsuarioAutenticado;

import java.util.UUID;

public interface UsuarioAutenticadoProviderService {
    UsuarioAutenticado usuarioAutenticado(OAuthUser oauthUser);

    GrupoAutenticado grupo(OAuthUser oauthUser);

    /**
     * Persiste nome local e invalida cache do usuário autenticado.
     */
    void atualizarNome(UUID usuarioId, String nome, OAuthUser oauthUser);
}
