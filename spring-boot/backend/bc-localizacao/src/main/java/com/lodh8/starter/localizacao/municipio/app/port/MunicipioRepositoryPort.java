package com.lodh8.starter.localizacao.municipio.app.port;

import com.lodh8.starter.localizacao.municipio.domain.Municipio;
import com.lodh8.starter.localizacao.municipio.domain.MunicipioRegister;
import com.lodh8.starter.localizacao.municipio.domain.MunicipioSearch;
import com.lodh8.starter.localizacao.municipio.domain.MunicipioUpdate;

import java.util.Collection;
import java.util.Optional;

public interface MunicipioRepositoryPort {

    Collection<Municipio> findAll(MunicipioSearch requestEntity);

    Optional<Municipio> findById(Long id);

    Municipio register(MunicipioRegister requestEntity);

    void update(Long id, MunicipioUpdate requestEntity);

    void delete(Long id);

    Optional<Municipio> findByNome(String nome, String uf);
}
