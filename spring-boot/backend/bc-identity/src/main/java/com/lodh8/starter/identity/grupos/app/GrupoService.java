package com.lodh8.starter.identity.grupos.app;

import com.lodh8.starter.identity.grupos.domain.Grupo;
import com.lodh8.starter.identity.grupos.domain.GrupoRegister;
import com.lodh8.starter.identity.grupos.domain.GrupoSearch;
import com.lodh8.starter.identity.grupos.domain.GrupoUpdate;

import java.util.Collection;
import java.util.Optional;

public interface GrupoService {
    Collection<Grupo> findAll(GrupoSearch requestEntity);
    Optional<Grupo> findById(String id);

    Optional<Grupo> findByIdentityId(String identityId);
    Grupo register(GrupoRegister requestEntity);
    void update(String id, GrupoUpdate requestEntity);
    void delete(String id);

    boolean hasMembers(String id);
}
