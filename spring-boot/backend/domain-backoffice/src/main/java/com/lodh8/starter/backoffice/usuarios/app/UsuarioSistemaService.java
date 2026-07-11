package com.lodh8.starter.backoffice.usuarios.app;

import com.herculanoleo.sentinelflow.exceptions.ValidatorException;
import com.lodh8.starter.backoffice.usuarios.domain.*;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioSistemaService {
    Collection<UsuarioSistema> findAll(UsuarioSistemaSearch requestEntity);

    Optional<UsuarioSistema> findById(UUID id);

    Optional<UsuarioSistema> findByEmail(String email);

    Optional<UsuarioSistema> findByIdentityId(String identityId);

    Optional<UsuarioSistemaGrupo> findGrupoById(UUID id);

    UsuarioSistema register(UsuarioSistemaRegister requestEntity) throws ValidatorException;

    void update(UUID id, UsuarioSistemaUpdate requestEntity) throws ValidatorException;

    void ativar(UUID id);

    void inativar(UUID id);

    void resetPassword(UUID id);

}
