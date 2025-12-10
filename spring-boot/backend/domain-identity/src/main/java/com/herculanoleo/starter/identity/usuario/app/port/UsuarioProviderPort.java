package com.herculanoleo.starter.identity.usuario.app.port;

import com.herculanoleo.starter.identity.usuario.domain.*;
import com.herculanoleo.starter.shared.models.enums.TipoAcesso;

import java.util.Optional;

public interface UsuarioProviderPort {
    Optional<Usuario> findById(String id);

    Usuario register(UsuarioRegister requestEntity);

    void update(String id, UsuarioUpdate requestEntity);

    boolean existsByEmail(String email);

    void updateAdminRole(String id, TipoAcesso tipo, boolean main);

    void ativar(String id);

    void inativar(String id);

    void delete(String id);

    void resetPassword(String id, RedirectAction requestEntity);

    void changePassword(String id, TrocaSenha requestEntity);

    void invalidateSessions(String id);
}
