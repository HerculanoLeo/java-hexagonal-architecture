package com.herculanoleo.starter.authorize.app;

import com.herculanoleo.starter.authorize.domain.GrupoAutenticado;
import com.herculanoleo.starter.authorize.domain.UsuarioAutenticado;

public interface UsuarioAutenticadoService {
    UsuarioAutenticado usuarioAutenticado();

    GrupoAutenticado grupoAutenticado();
}
