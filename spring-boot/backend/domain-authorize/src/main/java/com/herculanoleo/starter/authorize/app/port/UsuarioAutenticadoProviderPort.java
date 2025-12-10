package com.herculanoleo.starter.authorize.app.port;

import com.herculanoleo.starter.authorize.domain.GrupoAutenticado;
import com.herculanoleo.starter.authorize.domain.OAuthUser;
import com.herculanoleo.starter.authorize.domain.UsuarioAutenticado;

public interface UsuarioAutenticadoProviderPort {
    UsuarioAutenticado usuarioAutenticado(OAuthUser oauthUser);

    GrupoAutenticado grupo(OAuthUser oauthUser);
}
