package com.lodh8.starter.backoffice.usuarios.infra.persistence;

import com.lodh8.starter.shared.models.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.UpdateTimestamp;
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
@Table(name = "tb_sistema_usuario", schema = "plataformadmin")
public class UsuarioSistemaEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 5878065493403658120L;

    @Id
    @Generated(event = EventType.INSERT)
    @Column(name = "id", nullable = false, updatable = false, insertable = false)
    private UUID id;

    @Column(name = "id_identity", nullable = false)
    private String identityId;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "nome", nullable = false)
    private String nome;

    /** Usuário principal: espelha a role admin-sistemas no Keycloak. */
    @Column(name = "main", nullable = false)
    private boolean main;

    @Column(name = "status", nullable = false)
    private Status status;

    @Version
    @Column(name = "versao", nullable = false)
    private Integer versao;

    @CreationTimestamp
    @Column(name = "dt_criacao", nullable = false)
    private OffsetDateTime dataCriacao;

    @UpdateTimestamp
    @Column(name = "dt_atualizacao")
    private OffsetDateTime dataAtualizacao;
}
