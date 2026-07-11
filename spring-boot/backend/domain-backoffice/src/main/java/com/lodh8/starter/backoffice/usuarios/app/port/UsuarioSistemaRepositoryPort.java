package com.lodh8.starter.backoffice.usuarios.app.port;

import com.lodh8.starter.backoffice.usuarios.domain.UsuarioSistema;
import com.lodh8.starter.backoffice.usuarios.domain.UsuarioSistemaRegister;
import com.lodh8.starter.backoffice.usuarios.domain.UsuarioSistemaSearch;
import com.lodh8.starter.backoffice.usuarios.domain.UsuarioSistemaUpdate;

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

    /** Self-service: atualiza apenas o nome, sem alterar a flag main. */
    void updateNome(UUID id, String nome);

    void ativar(UUID id);

    void inativar(UUID id);
}
