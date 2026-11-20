package com.lodh8.starter.identity.usuario.app.impl;

import com.lodh8.starter.identity.usuario.app.PasswordConstraintService;
import com.lodh8.starter.identity.usuario.app.UsuarioService;
import com.lodh8.starter.identity.usuario.app.port.UsuarioProviderPort;
import com.lodh8.starter.identity.usuario.domain.*;
import com.lodh8.starter.identity.usuario.domain.events.UsuarioAtivadoEvent;
import com.lodh8.starter.identity.usuario.domain.events.UsuarioCriadoEvent;
import com.lodh8.starter.identity.usuario.domain.events.UsuarioInativadoEvent;
import com.lodh8.starter.shared.events.app.EventPublisherPort;
import com.lodh8.starter.shared.exceptions.BadRequestException;
import com.lodh8.starter.shared.exceptions.ConflictException;
import com.lodh8.starter.shared.exceptions.NotFoundException;
import com.lodh8.starter.shared.models.enums.TipoAcesso;
import com.lodh8.starter.shared.utils.IdentificadorLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioProviderPort usuarioProvider;

    private final PasswordConstraintService passwordConstraintService;

    private final EventPublisherPort events;

    @Override
    public Optional<Usuario> findById(String id) {
        log.info("usuario findById id={}", IdentificadorLog.mascarar(id));
        log.debug("usuario findById id={}", id);
        var result = usuarioProvider.findById(id);
        if (result.isEmpty()) {
            log.warn("usuario findById id={} resultado=nao_encontrado", IdentificadorLog.mascarar(id));
        }
        return result;
    }

    @Override
    public Usuario register(UsuarioRegister requestEntity) {
        log.info("usuario register inicio email={}", IdentificadorLog.mascarar(requestEntity.email()));
        log.debug("usuario register inicio email={} tipo={}", requestEntity.email(), requestEntity.tipo());
        if (usuarioProvider.existsByEmail(requestEntity.email())) {
            log.warn("usuario register conflito email={}", IdentificadorLog.mascarar(requestEntity.email()));
            throw new ConflictException("e-mail já cadastrado");
        }

        if (StringUtils.isNotBlank(requestEntity.senha())) {
            passwordConstraintService.validate(requestEntity.senha());
        }

        var usuario = usuarioProvider.register(requestEntity);

        usuarioProvider.updateAdminRole(usuario.id(), usuario.tipo(), requestEntity.main());

        events.publishEvent(new UsuarioCriadoEvent(usuario));

        log.info("usuario register fim id={}", IdentificadorLog.mascarar(usuario.id()));
        log.debug("usuario register fim id={} email={}", usuario.id(), usuario.email());
        return usuario;
    }

    @Override
    public void update(String id, UsuarioUpdate requestEntity) {
        log.info("usuario update inicio id={}", IdentificadorLog.mascarar(id));
        log.debug("usuario update inicio id={}", id);
        var usuario = this.findById(id).orElseThrow(() -> new NotFoundException("usuário não encontrado"));
        usuarioProvider.update(id, requestEntity);
        usuarioProvider.updateAdminRole(usuario.id(), usuario.tipo(), requestEntity.main());
        log.info("usuario update fim id={}", IdentificadorLog.mascarar(id));
        log.debug("usuario update fim id={}", id);
    }

    @Override
    public boolean hasAdminRole(String id, TipoAcesso tipo) {
        log.info("usuario hasAdminRole id={} tipo={}", IdentificadorLog.mascarar(id), tipo);
        log.debug("usuario hasAdminRole id={} tipo={}", id, tipo);
        this.findById(id).orElseThrow(() -> new NotFoundException("usuário não encontrado"));
        var has = usuarioProvider.hasAdminRole(id, tipo);
        log.info("usuario hasAdminRole id={} tipo={} resultado={}", IdentificadorLog.mascarar(id), tipo, has);
        return has;
    }

    @Override
    public void updateNome(String id, String nome) {
        log.info("usuario updateNome id={}", IdentificadorLog.mascarar(id));
        log.debug("usuario updateNome id={} nome={}", id, nome);
        this.findById(id).orElseThrow(() -> new NotFoundException("usuário não encontrado"));
        usuarioProvider.update(id, new UsuarioUpdate(false, nome, null, null));
        log.info("usuario updateNome fim id={}", IdentificadorLog.mascarar(id));
    }

    @Override
    public void ativar(String id) {
        log.info("usuario ativar inicio id={}", IdentificadorLog.mascarar(id));
        log.debug("usuario ativar inicio id={}", id);
        var usuario = this.findById(id).orElseThrow(() -> new NotFoundException("usuário não encontrado"));

        if (usuario.enabled()) {
            log.warn("usuario ativar id={} resultado=ja_ativo", IdentificadorLog.mascarar(id));
            throw new BadRequestException("usuário já ativo");
        }

        this.usuarioProvider.ativar(id);
        events.publishEvent(new UsuarioAtivadoEvent(id));
        log.info("usuario ativar fim id={}", IdentificadorLog.mascarar(id));
    }

    @Override
    public void inativar(String id) {
        log.info("usuario inativar inicio id={}", IdentificadorLog.mascarar(id));
        log.debug("usuario inativar inicio id={}", id);
        var usuario = this.findById(id).orElseThrow(() -> new NotFoundException("usuário não encontrado"));
        if (!usuario.enabled()) {
            log.warn("usuario inativar id={} resultado=ja_inativo", IdentificadorLog.mascarar(id));
            throw new BadRequestException("usuário já inativo");
        }
        this.usuarioProvider.inativar(id);
        events.publishEvent(new UsuarioInativadoEvent(id));
        log.info("usuario inativar fim id={}", IdentificadorLog.mascarar(id));
    }

    @Override
    public void delete(String id) {
        log.info("usuario delete inicio id={}", IdentificadorLog.mascarar(id));
        log.debug("usuario delete inicio id={}", id);
        findById(id).orElseThrow(() -> new NotFoundException("usuário não encontrado"));
        this.usuarioProvider.delete(id);
        log.info("usuario delete fim id={}", IdentificadorLog.mascarar(id));
    }

    @Override
    public void resetPassword(String id, RedirectAction requestEntity) {
        log.info("usuario resetPassword inicio id={}", IdentificadorLog.mascarar(id));
        log.debug("usuario resetPassword inicio id={}", id);
        findById(id).orElseThrow(() -> new NotFoundException("usuário não encontrado"));
        this.usuarioProvider.resetPassword(id, requestEntity);
        log.info("usuario resetPassword fim id={}", IdentificadorLog.mascarar(id));
    }

    @Override
    public void changePassword(String id, TrocaSenha requestEntity) {
        log.info("usuario changePassword inicio id={}", IdentificadorLog.mascarar(id));
        log.debug("usuario changePassword inicio id={}", id);
        if (null == requestEntity.novaSenha()
                || !requestEntity.novaSenha().equals(requestEntity.confirmacaoSenha())) {
            log.warn("usuario changePassword id={} resultado=confirmacao_invalida", IdentificadorLog.mascarar(id));
            throw new BadRequestException("Senha e Confirma Senha não são iguais");
        }

        this.passwordConstraintService.validate(requestEntity.novaSenha());

        findById(id).orElseThrow(() -> new NotFoundException("usuário não encontrado"));

        usuarioProvider.changePassword(id, requestEntity);

        invalidateSessions(id);
        log.info("usuario changePassword fim id={}", IdentificadorLog.mascarar(id));
    }

    @Override
    public void invalidateSessions(String id) {
        log.info("usuario invalidateSessions id={}", IdentificadorLog.mascarar(id));
        log.debug("usuario invalidateSessions id={}", id);
        usuarioProvider.invalidateSessions(id);
        log.info("usuario invalidateSessions fim id={}", IdentificadorLog.mascarar(id));
    }
}
