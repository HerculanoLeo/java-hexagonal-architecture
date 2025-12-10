package com.herculanoleo.starter.identity.grupos.app;

import com.herculanoleo.starter.identity.grupos.domain.Grupo;
import com.herculanoleo.starter.identity.grupos.domain.GrupoRegister;
import com.herculanoleo.starter.identity.grupos.domain.GrupoSearch;
import com.herculanoleo.starter.identity.grupos.domain.GrupoUpdate;

import java.util.Collection;
import java.util.Optional;

public interface GrupoService {
    Collection<Grupo> findAll(GrupoSearch requestEntity);
    Optional<Grupo> findById(String id);

    Optional<Grupo> findByIdentityId(String identityId);
    Grupo register(GrupoRegister requestEntity);
    void update(String id, GrupoUpdate requestEntity);
    void delete(String id);
}
