package com.herculanoleo.starter.authorize.app.impl;

import com.herculanoleo.starter.authorize.app.UsuarioAutenticadoService;
import com.herculanoleo.starter.authorize.app.port.OauthUserProviderPort;
import com.herculanoleo.starter.authorize.app.port.UsuarioAutenticadoProviderPort;
import com.herculanoleo.starter.authorize.domain.GrupoAutenticado;
import com.herculanoleo.starter.authorize.domain.UsuarioAutenticado;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioAutenticadoServiceImpl implements UsuarioAutenticadoService {

    private final OauthUserProviderPort oauthUserProviderPort;

    private final UsuarioAutenticadoProviderPort usuarioAutenticadoProviderPort;

    @Override
    public UsuarioAutenticado usuarioAutenticado() {
        var user = oauthUserProviderPort.oauthUser();
        return usuarioAutenticadoProviderPort.usuarioAutenticado(user);
    }

    @Override
    public GrupoAutenticado grupoAutenticado() {
        var user = oauthUserProviderPort.oauthUser();
        return usuarioAutenticadoProviderPort.grupo(user);
    }

}
