package com.lodh8.starter.notificacao.app;

import com.lodh8.starter.notificacao.domain.NotificacaoConfiguracao;
import com.lodh8.starter.shared.models.enums.NotificacaoConfiguracaoCodigo;

import java.util.Optional;

public interface NotificacaoConfiguracaoService {

    Optional<NotificacaoConfiguracao> findByCodigo(NotificacaoConfiguracaoCodigo codigo);

}
