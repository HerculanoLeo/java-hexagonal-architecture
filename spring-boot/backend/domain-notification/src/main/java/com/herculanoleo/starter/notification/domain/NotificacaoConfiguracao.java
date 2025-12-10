package com.herculanoleo.starter.notification.domain;

import com.herculanoleo.starter.shared.models.enums.NotificacaoConfiguracaoCodigo;

import java.util.UUID;

public record NotificacaoConfiguracao(
        UUID id,
        NotificacaoConfiguracaoCodigo codigo,
        String valor
) {
}
