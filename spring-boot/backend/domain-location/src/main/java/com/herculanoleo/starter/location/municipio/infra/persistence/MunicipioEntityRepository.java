package com.herculanoleo.starter.location.municipio.infra.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MunicipioEntityRepository extends JpaRepository<MunicipioEntity, Long>, JpaSpecificationExecutor<MunicipioEntity> {
    @Query(value =
            """
                    SELECT mun.*
                    FROM tb_municipio mun
                    INNER JOIN tb_estado est ON mun.id_estado = est.id
                    WHERE UPPER(UNACCENT(mun.nome)) = UPPER(UNACCENT(:nome))
                    AND (UPPER(est.sigla) = UPPER(:uf) OR UPPER(UNACCENT(est.nome)) = UPPER(UNACCENT(:uf)))
                    ORDER BY mun.id LIMIT 1
                    """,
            nativeQuery = true)
    Optional<MunicipioEntity> findByNome(@Param("nome") String nome, @Param("uf") String uf);
}
