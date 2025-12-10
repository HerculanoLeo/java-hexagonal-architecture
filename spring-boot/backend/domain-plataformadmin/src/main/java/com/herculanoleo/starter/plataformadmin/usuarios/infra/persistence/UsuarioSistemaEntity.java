package com.herculanoleo.starter.plataformadmin.usuarios.infra.persistence;

import com.herculanoleo.starter.shared.models.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
@Table(name = "tb_sistema_usuario")
public class UsuarioSistemaEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 5878065493403658120L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "id_identity", nullable = false)
    private String identityId;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "nome", nullable = false)
    private String nome;

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
