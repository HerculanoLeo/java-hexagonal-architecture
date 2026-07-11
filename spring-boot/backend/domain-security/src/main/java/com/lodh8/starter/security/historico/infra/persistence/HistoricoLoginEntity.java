package com.lodh8.starter.security.historico.infra.persistence;

import com.lodh8.starter.shared.models.enums.TipoAcesso;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tb_historico_login", schema = "security")
public class HistoricoLoginEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -7221174035090342374L;

    @Id
    @Generated(event = EventType.INSERT)
    @Column(name = "id", nullable = false, updatable = false, insertable = false)
    private UUID id;

    @Column(name = "id_identity", nullable = false, length = 36)
    private String idIdentity;

    @Column(name = "id_usuario")
    private UUID idUsuario;

    @Column(name = "tipo", nullable = false, length = 2)
    private TipoAcesso tipo;

    @Column(name = "id_relacionado", length = 36)
    private String idRelacionado;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "nome", length = 255)
    private String nome;

    @Column(name = "ip", length = 64)
    private String ip;

    @Column(name = "user_agent", length = 512)
    private String userAgent;

    @Builder.Default
    @Column(name = "sucesso", nullable = false)
    private boolean sucesso = true;

    @Builder.Default
    @Column(name = "dt_evento", nullable = false)
    private OffsetDateTime dataEvento = OffsetDateTime.now();

    @Column(name = "id_sessao_bff", length = 64)
    private String idSessaoBff;

}
