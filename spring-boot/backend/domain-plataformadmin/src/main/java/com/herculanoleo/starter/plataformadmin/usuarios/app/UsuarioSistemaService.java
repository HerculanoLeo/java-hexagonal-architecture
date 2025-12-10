package com.herculanoleo.starter.plataformadmin.usuarios.app;

import com.herculanoleo.starter.plataformadmin.usuarios.domain.*;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioSistemaService {
    Collection<UsuarioSistema> findAll(UsuarioSistemaSearch requestEntity);

    Optional<UsuarioSistema> findById(UUID id);

    Optional<UsuarioSistema> findByEmail(String email);

    Optional<UsuarioSistema> findByIdentityId(String identityId);

    Optional<UsuarioSistemaGrupo> findGrupoById(UUID id);

    UsuarioSistema register(UsuarioSistemaRegister requestEntity);

    void update(UUID id, UsuarioSistemaUpdate requestEntity);

    void ativar(UUID id);

    void inativar(UUID id);

    void resetPassword(UUID id);

}
