package com.lodh8.starter.notification.app;

import com.lodh8.starter.notification.domain.NotificacaoConfiguracao;
import com.lodh8.starter.shared.models.enums.NotificacaoConfiguracaoCodigo;

import java.util.Optional;

public interface NotificacaoConfiguracaoService {

    Optional<NotificacaoConfiguracao> findByCodigo(NotificacaoConfiguracaoCodigo codigo);

}
