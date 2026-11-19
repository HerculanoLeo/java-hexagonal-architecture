package com.lodh8.starter.notificacao.app.impl;

import com.lodh8.starter.notificacao.app.NotificacaoConfiguracaoService;
import com.lodh8.starter.notificacao.app.port.NotificacaoConfiguracaoRepositoryPort;
import com.lodh8.starter.notificacao.domain.NotificacaoConfiguracao;
import com.lodh8.starter.shared.models.enums.NotificacaoConfiguracaoCodigo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificacaoConfiguracaoServiceImpl implements NotificacaoConfiguracaoService {

    private final NotificacaoConfiguracaoRepositoryPort repository;

    @Override
    public Optional<NotificacaoConfiguracao> findByCodigo(NotificacaoConfiguracaoCodigo codigo) {
        return repository.findByCodigo(codigo);
    }
}
