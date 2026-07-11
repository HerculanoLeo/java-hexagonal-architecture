package com.lodh8.starter.notification.domain;

import com.lodh8.starter.shared.models.enums.NotificacaoConfiguracaoCodigo;

import java.util.UUID;

public record NotificacaoConfiguracao(
        UUID id,
        NotificacaoConfiguracaoCodigo codigo,
        String valor
) {
}
