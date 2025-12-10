package com.herculanoleo.starter.notification.app.ports;

import com.herculanoleo.starter.notification.domain.NotificacaoConfiguracao;
import com.herculanoleo.starter.shared.models.enums.NotificacaoConfiguracaoCodigo;

import java.util.Optional;

public interface NotificacaoConfiguracaoRepositoryPort {
    Optional<NotificacaoConfiguracao> findByCodigo(NotificacaoConfiguracaoCodigo codigo);
}
