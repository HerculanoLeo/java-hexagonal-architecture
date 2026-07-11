package com.lodh8.starter.security.sessao.app.impl;

import com.lodh8.starter.identity.usuario.app.UsuarioService;
import com.lodh8.starter.security.sessao.app.SessaoService;
import com.lodh8.starter.shared.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessaoServiceImpl implements SessaoService {

    private final UsuarioService usuarioService;

    @Override
    public void invalidateByIdentityId(String identityId) {
        this.usuarioService.findById(identityId)
                .orElseThrow(() -> new NotFoundException("usuário não encontrado"));
        this.usuarioService.invalidateSessions(identityId);
    }

}
