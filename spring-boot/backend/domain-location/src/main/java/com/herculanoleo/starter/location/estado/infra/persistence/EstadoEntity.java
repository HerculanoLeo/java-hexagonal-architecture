package com.herculanoleo.starter.location.estado.infra.persistence;

import com.herculanoleo.starter.shared.models.enums.EstadoSigla;
import com.herculanoleo.starter.shared.models.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tb_estado")
public class EstadoEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 526922611890555409L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_ESTADO")
    @SequenceGenerator(name = "SQ_ESTADO", sequenceName = "SQ_ESTADO", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "sigla", nullable = false, length = 2)
    private EstadoSigla sigla;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "status", nullable = false)
    private Status status;

}
