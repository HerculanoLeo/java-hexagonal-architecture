package com.lodh8.starter.location.cep.app;

import com.lodh8.starter.location.cep.domain.CEP;

import java.util.Optional;

public interface CepService {
    Optional<CEP> findByCep(String cep);
}
