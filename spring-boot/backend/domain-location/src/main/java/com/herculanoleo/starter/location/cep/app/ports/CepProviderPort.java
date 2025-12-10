package com.herculanoleo.starter.location.cep.app.ports;

import com.herculanoleo.starter.location.cep.domain.CEP;

import java.util.Optional;

public interface CepProviderPort {
    Optional<CEP> findByCep(String cep);
}
