package com.lodh8.starter.authorize.app;

import com.lodh8.starter.authorize.domain.GrupoAutenticado;
import com.lodh8.starter.authorize.domain.UsuarioAutenticado;
import com.lodh8.starter.identity.usuario.domain.TrocaSenha;

public interface UsuarioAutenticadoService {
    UsuarioAutenticado usuarioAutenticado();

    GrupoAutenticado grupoAutenticado();

    UsuarioAutenticado updateMe(String nome);

    void changePassword(TrocaSenha trocaSenha);
}
