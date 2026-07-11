package com.lodh8.starter.notification.api.dto;

import com.lodh8.starter.shared.models.enums.NotificacaoStatus;
import com.lodh8.starter.shared.models.enums.TipoNotificacao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificacaoDTO {

    private UUID id;

    private String titulo;

    private String conteudo;

    private Collection<String> destinatarios;

    private OffsetDateTime dataSolicitacao;

    private OffsetDateTime dataEnvio;

    private NotificacaoStatus status;

    private TipoNotificacao tipo;

    private Integer tentativas;

    private OffsetDateTime dataCriacao;

    private OffsetDateTime dataAtualizacao;

    private Map<String, Object> metadados;

}
