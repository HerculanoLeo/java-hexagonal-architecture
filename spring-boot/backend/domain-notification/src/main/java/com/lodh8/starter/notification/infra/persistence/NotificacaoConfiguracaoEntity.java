package com.lodh8.starter.notification.infra.persistence;

import com.lodh8.starter.shared.models.enums.NotificacaoConfiguracaoCodigo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tb_notificacao_configuracao", schema = "notification")
public class NotificacaoConfiguracaoEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 8487576982823017592L;

    @Id
    @Generated(event = EventType.INSERT)
    @Column(name = "id", nullable = false, updatable = false, insertable = false)
    private UUID id;

    @Column(name = "codigo", nullable = false)
    private NotificacaoConfiguracaoCodigo codigo;

    @Column(name = "valor", nullable = false)
    private String valor;

}