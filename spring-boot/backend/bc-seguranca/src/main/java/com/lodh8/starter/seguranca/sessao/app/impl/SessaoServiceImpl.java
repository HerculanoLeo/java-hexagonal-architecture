package com.lodh8.starter.seguranca.sessao.app.impl;

import com.lodh8.starter.identity.usuario.app.UsuarioService;
import com.lodh8.starter.seguranca.sessao.app.SessaoService;
import com.lodh8.starter.shared.exceptions.NotFoundException;
import com.lodh8.starter.shared.utils.IdentificadorLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SessaoServiceImpl implements SessaoService {

    private final UsuarioService usuarioService;

    @Override
    public void invalidateByIdentityId(String identityId) {
        log.info("sessao invalidateByIdentityId inicio identityId={}", IdentificadorLog.mascarar(identityId));
        log.debug("sessao invalidateByIdentityId inicio identityId={}", identityId);
        this.usuarioService.findById(identityId)
                .orElseThrow(() -> {
                    log.warn("sessao invalidateByIdentityId identityId={} resultado=nao_encontrado", IdentificadorLog.mascarar(identityId));
                    return new NotFoundException("usuário não encontrado");
                });
        this.usuarioService.invalidateSessions(identityId);
        log.info("sessao invalidateByIdentityId fim identityId={}", IdentificadorLog.mascarar(identityId));
        log.debug("sessao invalidateByIdentityId fim identityId={}", identityId);
    }

}