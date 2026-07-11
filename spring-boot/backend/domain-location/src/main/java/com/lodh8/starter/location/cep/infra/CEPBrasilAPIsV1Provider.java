package com.lodh8.starter.location.cep.infra;

import com.lodh8.starter.location.cep.app.port.CepProviderPort;
import com.lodh8.starter.location.cep.domain.CEP;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Order(3)
@RequiredArgsConstructor
public class CEPBrasilAPIsV1Provider implements CepProviderPort {

    protected final static String URL = "https://brasilapi.com.br/api/cep/v1/%s";

    private final CEPCacheService cacheService;

    @Override
    public Optional<CEP> findByCep(String cep) {
        return cacheService.doRequest(URL, cep);
    }

}
