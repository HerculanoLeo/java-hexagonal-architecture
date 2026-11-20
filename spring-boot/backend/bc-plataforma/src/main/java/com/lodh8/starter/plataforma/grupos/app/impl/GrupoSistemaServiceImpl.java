package com.lodh8.starter.plataforma.grupos.app.impl;

import com.herculanoleo.sentinelflow.exceptions.ValidatorException;
import com.lodh8.starter.identity.grupos.app.GrupoService;
import com.lodh8.starter.plataforma.grupos.app.GrupoSistemaRegisterValidator;
import com.lodh8.starter.plataforma.grupos.app.GrupoSistemaService;
import com.lodh8.starter.plataforma.grupos.app.GrupoSistemaUpdateValidator;
import com.lodh8.starter.plataforma.grupos.app.port.GrupoSistemaMapperPort;
import com.lodh8.starter.plataforma.grupos.domain.GrupoSistema;
import com.lodh8.starter.plataforma.grupos.domain.GrupoSistemaRegister;
import com.lodh8.starter.plataforma.grupos.domain.GrupoSistemaSearch;
import com.lodh8.starter.plataforma.grupos.domain.GrupoSistemaUpdate;
import com.lodh8.starter.shared.exceptions.ConflictException;
import com.lodh8.starter.shared.utils.IdentificadorLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GrupoSistemaServiceImpl implements GrupoSistemaService {

    private final GrupoService grupoService;

    private final GrupoSistemaMapperPort mapper;

    private final GrupoSistemaRegisterValidator registerValidator;

    private final GrupoSistemaUpdateValidator updateValidator;

    @Override
    public Collection<GrupoSistema> findAll(GrupoSistemaSearch requestEntity) {
        log.info("grupoSistema findAll inicio");
        log.debug("grupoSistema findAll inicio search={}", requestEntity);
        var result = this.grupoService.findAll(mapper.search(requestEntity)).stream()
                .map(mapper::domain)
                .toList();
        log.info("grupoSistema findAll fim total={}", result.size());
        return result;
    }

    @Override
    public Optional<GrupoSistema> findById(String id) {
        log.info("grupoSistema findById id={}", IdentificadorLog.mascarar(id));
        log.debug("grupoSistema findById id={}", id);
        var result = this.grupoService.findById(id).map(mapper::domain);
        if (result.isEmpty()) {
            log.warn("grupoSistema findById id={} resultado=nao_encontrado", IdentificadorLog.mascarar(id));
        }
        return result;
    }

    @Override
    public Optional<GrupoSistema> findByIdentityId(String identityId) {
        log.info("grupoSistema findByIdentityId identityId={}", IdentificadorLog.mascarar(identityId));
        log.debug("grupoSistema findByIdentityId identityId={}", identityId);
        var result = this.grupoService.findByIdentityId(identityId).map(mapper::domain);
        if (result.isEmpty()) {
            log.warn("grupoSistema findByIdentityId identityId={} resultado=nao_encontrado", IdentificadorLog.mascarar(identityId));
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public GrupoSistema register(GrupoSistemaRegister requestEntity) throws ValidatorException {
        log.info("grupoSistema register inicio nome={}", IdentificadorLog.mascarar(requestEntity.nome()));
        log.debug("grupoSistema register inicio nome={}", requestEntity.nome());
        this.registerValidator.validate(requestEntity);

        var grupo = this.grupoService.register(mapper.register(requestEntity));
        var domain = mapper.domain(grupo);
        log.info("grupoSistema register fim id={}", IdentificadorLog.mascarar(domain.id()));
        log.debug("grupoSistema register fim id={}", domain.id());
        return domain;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void update(String id, GrupoSistemaUpdate requestEntity) throws ValidatorException {
        log.info("grupoSistema update inicio id={}", IdentificadorLog.mascarar(id));
        log.debug("grupoSistema update inicio id={}", id);
        this.updateValidator.validate(requestEntity);

        this.grupoService.update(id, mapper.update(requestEntity));
        log.info("grupoSistema update fim id={}", IdentificadorLog.mascarar(id));
        log.debug("grupoSistema update fim id={}", id);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void delete(String id) {
        log.info("grupoSistema delete inicio id={}", IdentificadorLog.mascarar(id));
        log.debug("grupoSistema delete inicio id={}", id);
        if (this.grupoService.hasMembers(id)) {
            log.warn("grupoSistema delete id={} resultado=conflito_membros", IdentificadorLog.mascarar(id));
            throw new ConflictException("não é possível excluir um grupo com usuários vinculados");
        }
        this.grupoService.delete(id);
        log.info("grupoSistema delete fim id={}", IdentificadorLog.mascarar(id));
        log.debug("grupoSistema delete fim id={}", id);
    }

}