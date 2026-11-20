package com.lodh8.starter.plataforma.usuarios.app.impl;

import com.herculanoleo.sentinelflow.exceptions.ValidatorException;
import com.lodh8.starter.identity.usuario.app.UsuarioService;
import com.lodh8.starter.identity.usuario.domain.UsuarioRegister;
import com.lodh8.starter.identity.usuario.domain.UsuarioUpdate;
import com.lodh8.starter.plataforma.grupos.app.GrupoSistemaService;
import com.lodh8.starter.plataforma.usuarios.app.UsuarioSistemaRegisterValidator;
import com.lodh8.starter.plataforma.usuarios.app.UsuarioSistemaService;
import com.lodh8.starter.plataforma.usuarios.app.UsuarioSistemaUpdateValidator;
import com.lodh8.starter.plataforma.usuarios.app.port.SistemaRedirectActionPort;
import com.lodh8.starter.plataforma.usuarios.app.port.UsuarioSistemaRepositoryPort;
import com.lodh8.starter.plataforma.usuarios.domain.*;
import com.lodh8.starter.plataforma.usuarios.domain.event.UsuarioSistemaAtivadoEvent;
import com.lodh8.starter.plataforma.usuarios.domain.event.UsuarioSistemaAtualizadoEvent;
import com.lodh8.starter.plataforma.usuarios.domain.event.UsuarioSistemaCriadoEvent;
import com.lodh8.starter.plataforma.usuarios.domain.event.UsuarioSistemaInativadoEvent;
import com.lodh8.starter.shared.events.app.EventPublisherPort;
import com.lodh8.starter.shared.exceptions.ConflictException;
import com.lodh8.starter.shared.exceptions.NotFoundException;
import com.lodh8.starter.shared.models.enums.TipoAcesso;
import com.lodh8.starter.shared.utils.IdentificadorLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioSistemaServiceImpl implements UsuarioSistemaService {

    private final UsuarioService usuarioService;

    private final GrupoSistemaService grupoSistemaService;

    private final SistemaRedirectActionPort redirectActionPort;

    private final UsuarioSistemaRepositoryPort repository;

    private final EventPublisherPort events;

    private final UsuarioSistemaRegisterValidator registerValidator;

    private final UsuarioSistemaUpdateValidator updateValidator;

    @Override
    public Collection<UsuarioSistema> findAll(UsuarioSistemaSearch requestEntity) {
        log.info("usuarioSistema findAll inicio");
        log.debug("usuarioSistema findAll inicio search={}", requestEntity);
        var result = this.repository.findAll(requestEntity);
        log.info("usuarioSistema findAll fim total={}", result.size());
        return result;
    }

    public Optional<UsuarioSistema> findById(UUID id) {
        log.info("usuarioSistema findById id={}", IdentificadorLog.mascarar(id));
        log.debug("usuarioSistema findById id={}", id);
        var result = this.repository.findById(id);
        if (result.isEmpty()) {
            log.warn("usuarioSistema findById id={} resultado=nao_encontrado", IdentificadorLog.mascarar(id));
        }
        return result;
    }

    public Optional<UsuarioSistema> findByEmail(String email) {
        log.info("usuarioSistema findByEmail email={}", IdentificadorLog.mascarar(email));
        log.debug("usuarioSistema findByEmail email={}", email);
        var result = this.repository.findByEmail(email);
        if (result.isEmpty()) {
            log.warn("usuarioSistema findByEmail email={} resultado=nao_encontrado", IdentificadorLog.mascarar(email));
        }
        return result;
    }

    public Optional<UsuarioSistema> findByIdentityId(String identityId) {
        log.info("usuarioSistema findByIdentityId identityId={}", IdentificadorLog.mascarar(identityId));
        log.debug("usuarioSistema findByIdentityId identityId={}", identityId);
        var result = this.repository.findByIdentityId(identityId);
        if (result.isEmpty()) {
            log.warn("usuarioSistema findByIdentityId identityId={} resultado=nao_encontrado", IdentificadorLog.mascarar(identityId));
        }
        return result;
    }

    public Optional<UsuarioSistemaGrupo> findGrupoById(UUID id) {
        log.info("usuarioSistema findGrupoById id={}", IdentificadorLog.mascarar(id));
        log.debug("usuarioSistema findGrupoById id={}", id);
        var result = this.repository.findById(id)
                .flatMap(e -> this.grupoSistemaService.findByIdentityId(e.identityId()))
                .map(e -> new UsuarioSistemaGrupo(e.id(), e.nome()));
        if (result.isEmpty()) {
            log.warn("usuarioSistema findGrupoById id={} resultado=nao_encontrado", IdentificadorLog.mascarar(id));
        }
        return result;
    }

    @Transactional(rollbackFor = Throwable.class)
    public UsuarioSistema register(UsuarioSistemaRegister requestEntity) throws ValidatorException {
        log.info("usuarioSistema register inicio email={}", IdentificadorLog.mascarar(requestEntity.email()));
        log.debug("usuarioSistema register inicio email={} main={}", requestEntity.email(), requestEntity.main());
        this.registerValidator.validate(requestEntity);

        if (this.findByEmail(requestEntity.email()).isPresent()) {
            log.warn("usuarioSistema register conflito email={}", IdentificadorLog.mascarar(requestEntity.email()));
            throw new ConflictException("e-mail já cadastrado");
        }

        var grupoId = blankToNull(requestEntity.grupoId());

        var usuario = this.usuarioService.register(new UsuarioRegister(
                true,
                requestEntity.main(),
                true,
                requestEntity.nome(),
                requestEntity.email(),
                TipoAcesso.USUARIO_SISTEMA,
                grupoId,
                redirectActionPort.getRedirectAction()
        ));

        try {
            var usuarioSistema = this.repository.register(requestEntity.withIdentity(usuario.id()));

            events.publishEvent(new UsuarioSistemaCriadoEvent(usuarioSistema));

            log.info("usuarioSistema register fim id={}", IdentificadorLog.mascarar(usuarioSistema.id()));
            log.debug("usuarioSistema register fim id={}", usuarioSistema.id());
            return usuarioSistema;
        } catch (Exception e) {
            log.error("usuarioSistema register rollback identityId={}", IdentificadorLog.mascarar(usuario.id()), e);
            this.usuarioService.delete(usuario.id());
            throw e;
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public void update(UUID id, UsuarioSistemaUpdate requestEntity) throws ValidatorException {
        log.info("usuarioSistema update inicio id={}", IdentificadorLog.mascarar(id));
        log.debug("usuarioSistema update inicio id={}", id);
        this.updateValidator.validate(requestEntity);

        var entity = this.repository.findById(id).orElseThrow(() -> {
            log.warn("usuarioSistema update id={} resultado=nao_encontrado", IdentificadorLog.mascarar(id));
            return new NotFoundException("usuário não encontrado");
        });

        // null = não altera grupos (self-service); blank = remove grupos (main sem grupo).
        var grupoId = requestEntity.main()
                ? (requestEntity.grupoId() == null ? "" : requestEntity.grupoId().trim())
                : blankToNull(requestEntity.grupoId());

        this.usuarioService.update(entity.identityId(), new UsuarioUpdate(
                requestEntity.main(),
                requestEntity.nome(),
                entity.email(),
                grupoId
        ));

        this.repository.update(id, requestEntity);

        this.events.publishEvent(new UsuarioSistemaAtualizadoEvent(id));
        log.info("usuarioSistema update fim id={}", IdentificadorLog.mascarar(id));
        log.debug("usuarioSistema update fim id={}", id);
    }

    private static String blankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }

    @Transactional(rollbackFor = Throwable.class)
    public void ativar(UUID id) {
        log.info("usuarioSistema ativar inicio id={}", IdentificadorLog.mascarar(id));
        log.debug("usuarioSistema ativar inicio id={}", id);
        var entity = this.repository.findById(id).orElseThrow(() -> {
            log.warn("usuarioSistema ativar id={} resultado=nao_encontrado", IdentificadorLog.mascarar(id));
            return new NotFoundException("usuário não encontrado");
        });
        this.usuarioService.ativar(entity.identityId());
        this.repository.ativar(id);
        this.events.publishEvent(new UsuarioSistemaAtivadoEvent(entity));
        log.info("usuarioSistema ativar fim id={}", IdentificadorLog.mascarar(id));
        log.debug("usuarioSistema ativar fim id={}", id);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void inativar(UUID id) {
        log.info("usuarioSistema inativar inicio id={}", IdentificadorLog.mascarar(id));
        log.debug("usuarioSistema inativar inicio id={}", id);
        var entity = this.repository.findById(id).orElseThrow(() -> {
            log.warn("usuarioSistema inativar id={} resultado=nao_encontrado", IdentificadorLog.mascarar(id));
            return new NotFoundException("usuário não encontrado");
        });
        this.usuarioService.inativar(entity.identityId());
        this.repository.inativar(id);
        this.events.publishEvent(new UsuarioSistemaInativadoEvent(entity));
        log.info("usuarioSistema inativar fim id={}", IdentificadorLog.mascarar(id));
        log.debug("usuarioSistema inativar fim id={}", id);
    }

    public void resetPassword(UUID id) {
        log.info("usuarioSistema resetPassword inicio id={}", IdentificadorLog.mascarar(id));
        log.debug("usuarioSistema resetPassword inicio id={}", id);
        var entity = this.repository.findById(id).orElseThrow(() -> {
            log.warn("usuarioSistema resetPassword id={} resultado=nao_encontrado", IdentificadorLog.mascarar(id));
            return new NotFoundException("usuário não encontrado");
        });
        this.usuarioService.resetPassword(entity.identityId(), redirectActionPort.getRedirectAction());
        log.info("usuarioSistema resetPassword fim id={}", IdentificadorLog.mascarar(id));
        log.debug("usuarioSistema resetPassword fim id={}", id);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void updateNome(UUID id, String nome) {
        log.info("usuarioSistema updateNome inicio id={}", IdentificadorLog.mascarar(id));
        log.debug("usuarioSistema updateNome inicio id={}", id);
        this.repository.updateNome(id, nome);
        log.info("usuarioSistema updateNome fim id={}", IdentificadorLog.mascarar(id));
        log.debug("usuarioSistema updateNome fim id={}", id);
    }

}