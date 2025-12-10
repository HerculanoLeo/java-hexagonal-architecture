package com.herculanoleo.starter.location.cep.app;

import com.herculanoleo.starter.location.cep.domain.CEP;

import java.util.Optional;

public interface CepService {
    Optional<CEP> findByCep(String cep);
}
