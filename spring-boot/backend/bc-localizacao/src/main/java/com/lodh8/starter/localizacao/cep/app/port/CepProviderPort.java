package com.lodh8.starter.localizacao.cep.app.port;

import com.lodh8.starter.localizacao.cep.domain.CEP;

import java.util.Optional;

public interface CepProviderPort {
    Optional<CEP> findByCep(String cep);
}
