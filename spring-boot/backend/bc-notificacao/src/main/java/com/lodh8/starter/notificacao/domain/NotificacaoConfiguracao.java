package com.lodh8.starter.notificacao.domain;

import com.lodh8.starter.shared.models.enums.NotificacaoConfiguracaoCodigo;

import java.util.UUID;

public record NotificacaoConfiguracao(
        UUID id,
        NotificacaoConfiguracaoCodigo codigo,
        String valor
) {
}
