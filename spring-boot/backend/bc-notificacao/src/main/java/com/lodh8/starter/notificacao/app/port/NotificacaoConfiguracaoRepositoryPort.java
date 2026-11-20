package com.lodh8.starter.notificacao.app.port;

import com.lodh8.starter.notificacao.domain.NotificacaoConfiguracao;
import com.lodh8.starter.shared.models.enums.NotificacaoConfiguracaoCodigo;

import java.util.Optional;

public interface NotificacaoConfiguracaoRepositoryPort {
    Optional<NotificacaoConfiguracao> findByCodigo(NotificacaoConfiguracaoCodigo codigo);
}
