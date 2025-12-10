package com.herculanoleo.starter.location.cep.infra;

import com.herculanoleo.starter.location.cep.app.ports.CepProviderPort;
import com.herculanoleo.starter.location.cep.domain.CEP;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Order(2)
@RequiredArgsConstructor
public class CEPBrasilAPIsV2Provider implements CepProviderPort {

    protected final static String URL = "https://brasilapi.com.br/api/cep/v2/%s";

    private final CEPCacheService cacheService;

    @Override
    public Optional<CEP> findByCep(String cep) {
        return cacheService.doRequest(URL, cep);
    }

}
