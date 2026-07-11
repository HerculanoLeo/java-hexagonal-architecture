package com.lodh8.starter.backoffice.usuarios.app.impl;

import com.herculanoleo.sentinelflow.exceptions.ValidatorException;
import com.lodh8.starter.backoffice.grupos.app.GrupoSistemaService;
import com.lodh8.starter.backoffice.usuarios.app.UsuarioSistemaRegisterValidator;
import com.lodh8.starter.backoffice.usuarios.app.UsuarioSistemaService;
import com.lodh8.starter.backoffice.usuarios.app.UsuarioSistemaUpdateValidator;
import com.lodh8.starter.backoffice.usuarios.app.port.SistemaRedirectActionPort;
import com.lodh8.starter.backoffice.usuarios.app.port.UsuarioSistemaRepositoryPort;
import com.lodh8.starter.backoffice.usuarios.domain.*;
import com.lodh8.starter.backoffice.usuarios.domain.event.UsuarioSistemaAtivadoEvent;
import com.lodh8.starter.backoffice.usuarios.domain.event.UsuarioSistemaAtualizadoEvent;
import com.lodh8.starter.backoffice.usuarios.domain.event.UsuarioSistemaCriadoEvent;
import com.lodh8.starter.backoffice.usuarios.domain.event.UsuarioSistemaInativadoEvent;
import com.lodh8.starter.identity.usuario.app.UsuarioService;
import com.lodh8.starter.identity.usuario.domain.UsuarioRegister;
import com.lodh8.starter.identity.usuario.domain.UsuarioUpdate;
import com.lodh8.starter.shared.events.app.EventPublisherPort;
import com.lodh8.starter.shared.exceptions.ConflictException;
import com.lodh8.starter.shared.exceptions.NotFoundException;
import com.lodh8.starter.shared.models.enums.TipoAcesso;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

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
        return this.repository.findAll(requestEntity);
    }

    public Optional<UsuarioSistema> findById(UUID id) {
        return this.repository.findById(id);
    }

    public Optional<UsuarioSistema> findByEmail(String email) {
        return this.repository.findByEmail(email);
    }

    public Optional<UsuarioSistema> findByIdentityId(String identityId) {
        return this.repository.findByIdentityId(identityId);
    }

    public Optional<UsuarioSistemaGrupo> findGrupoById(UUID id) {
        return this.repository.findById(id)
                .flatMap(e -> this.grupoSistemaService.findByIdentityId(e.identityId()))
                .map(e -> new UsuarioSistemaGrupo(e.id(), e.nome()));
    }

    @Transactional(rollbackFor = Throwable.class)
    public UsuarioSistema register(UsuarioSistemaRegister requestEntity) throws ValidatorException {
        this.registerValidator.validate(requestEntity);

        if (this.findByEmail(requestEntity.email()).isPresent()) {
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

            return usuarioSistema;
        } catch (Exception e) {
            this.usuarioService.delete(usuario.id());
            throw e;
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public void update(UUID id, UsuarioSistemaUpdate requestEntity) throws ValidatorException {
        this.updateValidator.validate(requestEntity);

        var entity = this.repository.findById(id).orElseThrow(() -> new NotFoundException("usuário não encontrado"));

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
    }

    private static String blankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }

    @Transactional(rollbackFor = Throwable.class)
    public void ativar(UUID id) {
        var entity = this.repository.findById(id).orElseThrow(() -> new NotFoundException("usuário não encontrado"));
        this.usuarioService.ativar(entity.identityId());
        this.repository.ativar(id);
        this.events.publishEvent(new UsuarioSistemaAtivadoEvent(entity));
    }

    @Transactional(rollbackFor = Throwable.class)
    public void inativar(UUID id) {
        var entity = this.repository.findById(id).orElseThrow(() -> new NotFoundException("usuário não encontrado"));
        this.usuarioService.inativar(entity.identityId());
        this.repository.inativar(id);
        this.events.publishEvent(new UsuarioSistemaInativadoEvent(entity));
    }

    public void resetPassword(UUID id) {
        var entity = this.repository.findById(id).orElseThrow(() -> new NotFoundException("usuário não encontrado"));
        this.usuarioService.resetPassword(entity.identityId(), redirectActionPort.getRedirectAction());
    }

}
