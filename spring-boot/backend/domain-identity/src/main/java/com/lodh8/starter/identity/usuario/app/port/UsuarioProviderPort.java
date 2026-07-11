package com.lodh8.starter.identity.usuario.app.port;

import com.lodh8.starter.identity.usuario.domain.*;
import com.lodh8.starter.shared.models.enums.TipoAcesso;

import java.util.Optional;

public interface UsuarioProviderPort {
    Optional<Usuario> findById(String id);

    Usuario register(UsuarioRegister requestEntity);

    void update(String id, UsuarioUpdate requestEntity);

    boolean existsByEmail(String email);

    void updateAdminRole(String id, TipoAcesso tipo, boolean main);

    boolean hasAdminRole(String id, TipoAcesso tipo);

    void ativar(String id);

    void inativar(String id);

    void delete(String id);

    void resetPassword(String id, RedirectAction requestEntity);

    void changePassword(String id, TrocaSenha requestEntity);

    void invalidateSessions(String id);
}
