package com.lodh8.starter.localizacao.municipio.app;

import com.lodh8.starter.localizacao.municipio.domain.Municipio;
import com.lodh8.starter.localizacao.municipio.domain.MunicipioRegister;
import com.lodh8.starter.localizacao.municipio.domain.MunicipioSearch;
import com.lodh8.starter.localizacao.municipio.domain.MunicipioUpdate;

import java.util.Collection;
import java.util.Optional;

public interface MunicipioService {

    Collection<Municipio> findAll(MunicipioSearch search);

    Optional<Municipio> findById(Long id);

    Municipio register(MunicipioRegister municipio);

    void update(Long id, MunicipioUpdate municipio);

    void delete(Long id);

    Optional<Municipio> findByNome(String nome, String uf);
}
