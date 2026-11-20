package com.lodh8.starter.localizacao.cep.app;

import com.lodh8.starter.localizacao.cep.domain.CEP;

import java.util.Optional;

public interface CepService {
    Optional<CEP> findByCep(String cep);
}
