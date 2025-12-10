package com.herculanoleo.starter.identity.usuario.app.impl;

import com.herculanoleo.starter.identity.usuario.app.PasswordConstraintService;
import com.herculanoleo.starter.identity.usuario.app.UsuarioService;
import com.herculanoleo.starter.identity.usuario.app.port.UsuarioProviderPort;
import com.herculanoleo.starter.identity.usuario.domain.*;
import com.herculanoleo.starter.identity.usuario.domain.events.UsuarioAtivadoEvent;
import com.herculanoleo.starter.identity.usuario.domain.events.UsuarioCriadoEvent;
import com.herculanoleo.starter.identity.usuario.domain.events.UsuarioInativadoEvent;
import com.herculanoleo.starter.shared.events.app.EventPublisherPort;
import com.herculanoleo.starter.shared.exceptions.BadRequestException;
import com.herculanoleo.starter.shared.exceptions.ConflictException;
import com.herculanoleo.starter.shared.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioProviderPort usuarioProvider;

    private final PasswordConstraintService passwordConstraintService;

    private final EventPublisherPort events;

    @Override
    public Optional<Usuario> findById(String id) {
        return usuarioProvider.findById(id);
    }

    @Override
    public Usuario register(UsuarioRegister requestEntity) {
        if (usuarioProvider.existsByEmail(requestEntity.email())) {
            throw new ConflictException("e-mail já cadastrado");
        }

        if (StringUtils.isNotBlank(requestEntity.senha())) {
            passwordConstraintService.validate(requestEntity.senha());
        }

        var usuario = usuarioProvider.register(requestEntity);

        usuarioProvider.updateAdminRole(usuario.id(), usuario.tipo(), requestEntity.main());

        events.publishEvent(new UsuarioCriadoEvent(usuario));

        return usuario;
    }

    @Override
    public void update(String id, UsuarioUpdate requestEntity) {
        var usuario = this.findById(id).orElseThrow(() -> new NotFoundException("usuário não encontrado"));
        usuarioProvider.update(id, requestEntity);
        usuarioProvider.updateAdminRole(usuario.id(), usuario.tipo(), requestEntity.main());
    }

    @Override
    public void ativar(String id) {
        var usuario = this.findById(id).orElseThrow(() -> new NotFoundException("usuário não encontrado"));

        if (usuario.enabled()) {
            throw new BadRequestException("usuário já ativo");
        }

        this.usuarioProvider.ativar(id);
        events.publishEvent(new UsuarioAtivadoEvent(id));
    }

    @Override
    public void inativar(String id) {
        var usuario = this.findById(id).orElseThrow(() -> new NotFoundException("usuário não encontrado"));
        if (!usuario.enabled()) {
            throw new BadRequestException("usuário já inativo");
        }
        this.usuarioProvider.inativar(id);
        events.publishEvent(new UsuarioInativadoEvent(id));
    }

    @Override
    public void delete(String id) {
        findById(id).orElseThrow(() -> new NotFoundException("usuário não encontrado"));
        this.usuarioProvider.delete(id);
    }

    @Override
    public void resetPassword(String id, RedirectAction requestEntity) {
        findById(id).orElseThrow(() -> new NotFoundException("usuário não encontrado"));
        this.usuarioProvider.resetPassword(id, requestEntity);
    }

    @Override
    public void changePassword(String id, TrocaSenha requestEntity) {
        if (null == requestEntity.novaSenha()
                || !requestEntity.novaSenha().equals(requestEntity.confirmacaoSenha())) {
            throw new BadRequestException("Senha e Confirma Senha não são iguais");
        }

        this.passwordConstraintService.validate(requestEntity.novaSenha());

        findById(id).orElseThrow(() -> new NotFoundException("usuário não encontrado"));

        usuarioProvider.changePassword(id, requestEntity);

        invalidateSessions(id);
    }

    @Override
    public void invalidateSessions(String id) {
        usuarioProvider.invalidateSessions(id);
    }
}
