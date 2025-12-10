package com.herculanoleo.starter.plataformadmin.grupos.app.impl;

import com.herculanoleo.starter.identity.grupos.app.GrupoService;
import com.herculanoleo.starter.plataformadmin.grupos.app.GrupoSistemaService;
import com.herculanoleo.starter.plataformadmin.grupos.app.port.GrupoSistemaMapperPort;
import com.herculanoleo.starter.plataformadmin.grupos.domain.GrupoSistema;
import com.herculanoleo.starter.plataformadmin.grupos.domain.GrupoSistemaRegister;
import com.herculanoleo.starter.plataformadmin.grupos.domain.GrupoSistemaSearch;
import com.herculanoleo.starter.plataformadmin.grupos.domain.GrupoSistemaUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GrupoSistemaServiceImpl implements GrupoSistemaService {

    private final GrupoService grupoService;

    private final GrupoSistemaMapperPort mapper;

    @Override
    public Collection<GrupoSistema> findAll(GrupoSistemaSearch requestEntity) {
        return this.grupoService.findAll(mapper.search(requestEntity)).stream()
                .map(mapper::domain)
                .toList();
    }

    @Override
    public Optional<GrupoSistema> findById(String id) {
        return this.grupoService.findById(id).map(mapper::domain);
    }

    @Override
    public Optional<GrupoSistema> findByIdentityId(String identityId) {
        return this.grupoService.findByIdentityId(identityId).map(mapper::domain);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public GrupoSistema register(GrupoSistemaRegister requestEntity) {
        var grupo = this.grupoService.register(mapper.register(requestEntity));
        return mapper.domain(grupo);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void update(String id, GrupoSistemaUpdate requestEntity) {
        this.grupoService.update(id, mapper.update(requestEntity));
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void delete(String id) {
        this.grupoService.delete(id);
    }

}
