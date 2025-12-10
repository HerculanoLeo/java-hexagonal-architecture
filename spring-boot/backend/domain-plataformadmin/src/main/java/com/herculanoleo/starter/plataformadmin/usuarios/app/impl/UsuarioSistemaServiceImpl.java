package com.herculanoleo.starter.plataformadmin.usuarios.app.impl;

import com.herculanoleo.starter.identity.usuario.app.UsuarioService;
import com.herculanoleo.starter.identity.usuario.domain.UsuarioRegister;
import com.herculanoleo.starter.identity.usuario.domain.UsuarioUpdate;
import com.herculanoleo.starter.plataformadmin.grupos.app.GrupoSistemaService;
import com.herculanoleo.starter.plataformadmin.usuarios.app.UsuarioSistemaService;
import com.herculanoleo.starter.plataformadmin.usuarios.app.port.SistemaRedirectActionPort;
import com.herculanoleo.starter.plataformadmin.usuarios.app.port.UsuarioSistemaRepositoryPort;
import com.herculanoleo.starter.plataformadmin.usuarios.domain.*;
import com.herculanoleo.starter.plataformadmin.usuarios.domain.event.UsuarioSistemaAtivadoEvent;
import com.herculanoleo.starter.plataformadmin.usuarios.domain.event.UsuarioSistemaAtualizadoEvent;
import com.herculanoleo.starter.plataformadmin.usuarios.domain.event.UsuarioSistemaCriadoEvent;
import com.herculanoleo.starter.plataformadmin.usuarios.domain.event.UsuarioSistemaInativadoEvent;
import com.herculanoleo.starter.shared.events.app.EventPublisherPort;
import com.herculanoleo.starter.shared.exceptions.ConflictException;
import com.herculanoleo.starter.shared.exceptions.NotFoundException;
import com.herculanoleo.starter.shared.models.enums.TipoAcesso;
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
    public UsuarioSistema register(UsuarioSistemaRegister requestEntity) {
        if (this.findByEmail(requestEntity.email()).isPresent()) {
            throw new ConflictException("e-mail já cadastrado");
        }

        var usuario = this.usuarioService.register(new UsuarioRegister(
                true,
                requestEntity.main(),
                true,
                requestEntity.nome(),
                requestEntity.email(),
                TipoAcesso.USUARIO_SISTEMA,
                requestEntity.grupoId(),
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
    public void update(UUID id, UsuarioSistemaUpdate requestEntity) {
        var entity = this.repository.findById(id).orElseThrow(() -> new NotFoundException("usuário não encontrado"));

        this.usuarioService.update(entity.identityId(), new UsuarioUpdate(
                requestEntity.main(),
                requestEntity.nome(),
                entity.email(),
                requestEntity.grupoId()
        ));

        this.repository.update(id, requestEntity);

        this.events.publishEvent(new UsuarioSistemaAtualizadoEvent(id));
    }

    @Transactional(rollbackFor = Throwable.class)
    public void ativar(UUID id) {
        var entity = this.repository.findById(id).orElseThrow(() -> new NotFoundException("usuário não encontrado"));
        this.usuarioService.ativar(entity.identityId());
        this.repository.ativar(id);
        this.events.publishEvent(new UsuarioSistemaAtivadoEvent(id));
    }

    @Transactional(rollbackFor = Throwable.class)
    public void inativar(UUID id) {
        var entity = this.repository.findById(id).orElseThrow(() -> new NotFoundException("usuário não encontrado"));
        this.usuarioService.inativar(entity.identityId());
        this.repository.inativar(id);
        this.events.publishEvent(new UsuarioSistemaInativadoEvent(id));
    }

    public void resetPassword(UUID id) {
        var entity = this.repository.findById(id).orElseThrow(() -> new NotFoundException("usuário não encontrado"));
        this.usuarioService.resetPassword(entity.identityId(), redirectActionPort.getRedirectAction());
    }

}
