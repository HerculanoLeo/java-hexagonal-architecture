package com.herculanoleo.starter.notification.app.impl;

import com.herculanoleo.starter.notification.app.NotificacaoConfiguracaoService;
import com.herculanoleo.starter.notification.app.ports.NotificacaoConfiguracaoRepositoryPort;
import com.herculanoleo.starter.notification.domain.NotificacaoConfiguracao;
import com.herculanoleo.starter.shared.models.enums.NotificacaoConfiguracaoCodigo;
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
