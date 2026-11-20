package com.lodh8.starter.notificacao.app.impl;

import com.lodh8.starter.notificacao.app.NotificacaoConfiguracaoService;
import com.lodh8.starter.notificacao.app.port.NotificacaoConfiguracaoRepositoryPort;
import com.lodh8.starter.notificacao.domain.NotificacaoConfiguracao;
import com.lodh8.starter.shared.models.enums.NotificacaoConfiguracaoCodigo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificacaoConfiguracaoServiceImpl implements NotificacaoConfiguracaoService {

    private final NotificacaoConfiguracaoRepositoryPort repository;

    @Override
    public Optional<NotificacaoConfiguracao> findByCodigo(NotificacaoConfiguracaoCodigo codigo) {
        log.info("notificacaoConfiguracao findByCodigo codigo={}", codigo);
        var result = repository.findByCodigo(codigo);
        if (result.isEmpty()) {
            log.warn("notificacaoConfiguracao findByCodigo codigo={} resultado=nao_encontrado", codigo);
        }
        return result;
    }
}