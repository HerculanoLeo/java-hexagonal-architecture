package com.lodh8.starter.location.cep.app.port;

import com.lodh8.starter.location.cep.domain.CEP;

import java.util.Optional;

public interface CepProviderPort {
    Optional<CEP> findByCep(String cep);
}
