package com.lodh8.starter.cadastros.infra.notification;

import com.lodh8.starter.authorize.app.UsuarioAutenticadoService;
import com.lodh8.starter.notificacao.app.NotificacaoUsuarioAtualService;
import com.lodh8.starter.notificacao.domain.NotificacaoUsuarioAtual;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificacaoUsuarioAtualAdapter implements NotificacaoUsuarioAtualService {

    private final UsuarioAutenticadoService usuarioAutenticadoService;

    @Override
    public NotificacaoUsuarioAtual usuarioAtual() {
        var usuario = usuarioAutenticadoService.usuarioAutenticado();
        return new NotificacaoUsuarioAtual(usuario.nome(), usuario.email());
    }

}
