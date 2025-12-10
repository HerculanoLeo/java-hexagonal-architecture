package com.herculanoleo.starter.plataformadmin.usuarios.app.port;

import com.herculanoleo.starter.plataformadmin.usuarios.domain.UsuarioSistema;
import com.herculanoleo.starter.plataformadmin.usuarios.domain.UsuarioSistemaRegister;
import com.herculanoleo.starter.plataformadmin.usuarios.domain.UsuarioSistemaSearch;
import com.herculanoleo.starter.plataformadmin.usuarios.domain.UsuarioSistemaUpdate;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioSistemaRepositoryPort {
    Collection<UsuarioSistema> findAll(UsuarioSistemaSearch requestEntity);

    Optional<UsuarioSistema> findById(UUID id);

    Optional<UsuarioSistema> findByEmail(String email);

    Optional<UsuarioSistema> findByIdentityId(String identityId);

    UsuarioSistema register(UsuarioSistemaRegister requestEntity);

    void update(UUID id, UsuarioSistemaUpdate requestEntity);

    void ativar(UUID id);

    void inativar(UUID id);
}
