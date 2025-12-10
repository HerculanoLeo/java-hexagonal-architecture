package com.herculanoleo.starter.plataformadmin.grupos.app;

import com.herculanoleo.starter.plataformadmin.grupos.domain.GrupoSistema;
import com.herculanoleo.starter.plataformadmin.grupos.domain.GrupoSistemaRegister;
import com.herculanoleo.starter.plataformadmin.grupos.domain.GrupoSistemaSearch;
import com.herculanoleo.starter.plataformadmin.grupos.domain.GrupoSistemaUpdate;

import java.util.Collection;
import java.util.Optional;

public interface GrupoSistemaService {
    Collection<GrupoSistema> findAll(GrupoSistemaSearch requestEntity);

    Optional<GrupoSistema> findById(String id);

    Optional<GrupoSistema> findByIdentityId(String identityId);

    GrupoSistema register(GrupoSistemaRegister requestEntity);

    void update(String id, GrupoSistemaUpdate requestEntity);

    void delete(String id);
}
