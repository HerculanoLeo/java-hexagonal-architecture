package com.herculanoleo.starter.location.cep.infra;

import com.herculanoleo.starter.location.cep.app.ports.CepProviderPort;
import com.herculanoleo.starter.location.cep.domain.CEP;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Primary
@Order(1)
@RequiredArgsConstructor
public class CEPOpenCepProvider implements CepProviderPort {

    protected final static String URL = "https://opencep.com/v1/%s";

    private final CEPCacheService cacheService;

    @Override
    public Optional<CEP> findByCep(String cep) {
        return cacheService.doRequest(URL, cep);
    }

}
