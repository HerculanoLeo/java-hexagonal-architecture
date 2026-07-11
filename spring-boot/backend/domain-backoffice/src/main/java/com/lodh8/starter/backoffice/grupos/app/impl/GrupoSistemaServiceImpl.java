package com.lodh8.starter.backoffice.grupos.app.impl;

import com.herculanoleo.sentinelflow.exceptions.ValidatorException;
import com.lodh8.starter.backoffice.grupos.app.GrupoSistemaRegisterValidator;
import com.lodh8.starter.backoffice.grupos.app.GrupoSistemaService;
import com.lodh8.starter.backoffice.grupos.app.GrupoSistemaUpdateValidator;
import com.lodh8.starter.backoffice.grupos.app.port.GrupoSistemaMapperPort;
import com.lodh8.starter.backoffice.grupos.domain.GrupoSistema;
import com.lodh8.starter.backoffice.grupos.domain.GrupoSistemaRegister;
import com.lodh8.starter.backoffice.grupos.domain.GrupoSistemaSearch;
import com.lodh8.starter.backoffice.grupos.domain.GrupoSistemaUpdate;
import com.lodh8.starter.identity.grupos.app.GrupoService;
import com.lodh8.starter.shared.exceptions.ConflictException;
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

    private final GrupoSistemaRegisterValidator registerValidator;

    private final GrupoSistemaUpdateValidator updateValidator;

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
    public GrupoSistema register(GrupoSistemaRegister requestEntity) throws ValidatorException {
        this.registerValidator.validate(requestEntity);

        var grupo = this.grupoService.register(mapper.register(requestEntity));
        return mapper.domain(grupo);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void update(String id, GrupoSistemaUpdate requestEntity) throws ValidatorException {
        this.updateValidator.validate(requestEntity);

        this.grupoService.update(id, mapper.update(requestEntity));
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void delete(String id) {
        if (this.grupoService.hasMembers(id)) {
            throw new ConflictException("não é possível excluir um grupo com usuários vinculados");
        }
        this.grupoService.delete(id);
    }

}
