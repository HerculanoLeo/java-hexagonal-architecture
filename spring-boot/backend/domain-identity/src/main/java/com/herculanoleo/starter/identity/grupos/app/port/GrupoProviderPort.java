package com.herculanoleo.starter.identity.grupos.app.port;

import com.herculanoleo.starter.identity.grupos.domain.Grupo;
import com.herculanoleo.starter.identity.grupos.domain.GrupoRegister;
import com.herculanoleo.starter.identity.grupos.domain.GrupoSearch;
import com.herculanoleo.starter.identity.grupos.domain.GrupoUpdate;
import com.herculanoleo.starter.shared.models.enums.TipoAcesso;

import java.util.Collection;
import java.util.Optional;

public interface GrupoProviderPort {
    Collection<Grupo> findAll(GrupoSearch requestEntity);
    Optional<Grupo> findById(String id);

    Optional<Grupo> findByIdentityId(String identityId);
    Grupo register(GrupoRegister requestEntity);
    void update(String id, GrupoUpdate requestEntity);
    void delete(String id);
    boolean existsByName(String nome);
    void updateRoles(String id, TipoAcesso tipo, Collection<String> newRoles);

}
