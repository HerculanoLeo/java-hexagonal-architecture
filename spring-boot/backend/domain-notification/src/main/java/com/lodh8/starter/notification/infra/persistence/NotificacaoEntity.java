package com.lodh8.starter.notification.infra.persistence;

import com.lodh8.starter.notification.domain.NotificacaoError;
import com.lodh8.starter.shared.models.enums.NotificacaoStatus;
import com.lodh8.starter.shared.models.enums.TipoNotificacao;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.generator.EventType;
import org.hibernate.type.SqlTypes;

import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tb_notificacao", schema = "notification")
public class NotificacaoEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -7657745346201430875L;

    @Id
    @Generated(event = EventType.INSERT)
    @Column(name = "id", nullable = false, updatable = false, insertable = false)
    private UUID id;

    @Column(name = "titulo", nullable = false, length = 100)
    private String titulo;

    @Column(name = "conteudo", nullable = false, length = Integer.MAX_VALUE)
    private String conteudo;

    @Column(name = "destinatarios", nullable = false, length = Integer.MAX_VALUE)
    private String destinatarios;

    @Builder.Default
    @Column(name = "dt_solicitacao", nullable = false)
    private OffsetDateTime dataSolicitacao = OffsetDateTime.now();

    @Column(name = "dt_envio")
    private OffsetDateTime dataEnvio;

    @Column(name = "status", nullable = false, length = Integer.MAX_VALUE)
    private NotificacaoStatus status;

    @Column(name = "tipo", nullable = false, length = 2)
    private TipoNotificacao tipo;

    @Builder.Default
    @Column(name = "tentativas", nullable = false)
    private Integer tentativas = 0;

    @Version
    @Column(name = "versao", nullable = false)
    private Integer versao;

    @CreationTimestamp
    @Column(name = "dt_criacao", nullable = false)
    private OffsetDateTime dataCriacao;

    @UpdateTimestamp
    @Column(name = "dt_atualizacao")
    private OffsetDateTime dataAtualizacao;

    @Column(name = "metadados")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> metadados;

    @Column(name = "errors")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<NotificacaoError> errors;

}