package com.herculanoleo.starter.identity.usuario.app;

import com.herculanoleo.starter.identity.usuario.domain.*;

import java.util.Optional;

public interface UsuarioService {
    Optional<Usuario> findById(String id);

    Usuario register(UsuarioRegister requestEntity);

    void update(String id, UsuarioUpdate requestEntity);

    void ativar(String id);

    void inativar(String id);

    void delete(String id);

    void resetPassword(String id, RedirectAction requestEntity);

    void changePassword(String id, TrocaSenha requestEntity);

    void invalidateSessions(String id);
}
