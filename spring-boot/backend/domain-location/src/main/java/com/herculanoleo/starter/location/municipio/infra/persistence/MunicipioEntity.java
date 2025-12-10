package com.herculanoleo.starter.location.municipio.infra.persistence;

import com.herculanoleo.starter.location.estado.infra.persistence.EstadoEntity;
import com.herculanoleo.starter.shared.models.enums.Status;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tb_municipio")
public class MunicipioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_MUNICIPIO")
    @SequenceGenerator(name = "SQ_MUNICIPIO", sequenceName = "SQ_MUNICIPIO", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "id_estado", nullable = false)
    private Long estadoId;

    @Column(name = "status", nullable = false)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estado", nullable = false, insertable = false, updatable = false)
    private EstadoEntity estado;

}
