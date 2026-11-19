package com.lodh8.starter.plataforma.grupos.app;

import com.herculanoleo.sentinelflow.exceptions.ValidatorException;
import com.lodh8.starter.plataforma.grupos.domain.GrupoSistema;
import com.lodh8.starter.plataforma.grupos.domain.GrupoSistemaRegister;
import com.lodh8.starter.plataforma.grupos.domain.GrupoSistemaSearch;
import com.lodh8.starter.plataforma.grupos.domain.GrupoSistemaUpdate;

import java.util.Collection;
import java.util.Optional;

public interface GrupoSistemaService {
    Collection<GrupoSistema> findAll(GrupoSistemaSearch requestEntity);

    Optional<GrupoSistema> findById(String id);

    Optional<GrupoSistema> findByIdentityId(String identityId);

    GrupoSistema register(GrupoSistemaRegister requestEntity) throws ValidatorException;

    void update(String id, GrupoSistemaUpdate requestEntity) throws ValidatorException;

    void delete(String id);
}
