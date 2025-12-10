package com.herculanoleo.starter.notification.infra.persistence;

import com.herculanoleo.starter.shared.models.enums.NotificacaoConfiguracaoCodigo;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tb_notificacao_configuracao")
public class NotificacaoConfiguracaoEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 8487576982823017592L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "codigo", nullable = false)
    private NotificacaoConfiguracaoCodigo codigo;

    @Column(name = "valor", nullable = false)
    private String valor;

}