package com.lodh8.starter.localizacao.cep.infra;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.lodh8.starter.localizacao.cep.domain.CEP;
import com.lodh8.starter.localizacao.cep.infra.dtos.CepResponse;
import com.lodh8.starter.localizacao.municipio.app.MunicipioService;
import com.lodh8.starter.shared.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.json.JsonMapper;

import java.util.Optional;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

// varias APIs de CEP tem limite de uso por IP, caso haja um pico de uso o IP da aplicação pode ser bloqueado
// a intenção dessa classe é evitar isso
@Service
public class CEPCacheService {

    protected final static Semaphore semaphore = new Semaphore(1024, true);

    protected final static String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36";

    protected final Cache<String, CEP> cache = Caffeine.newBuilder()
            .expireAfterWrite(1, TimeUnit.DAYS) // Expiração específica
            .maximumSize(10000)
            .build();

    protected final RestClient client;

    private final MunicipioService municipioService;

    @Autowired
    public CEPCacheService(JsonMapper objectMapper, MunicipioService municipioService) {
        this(restClientBuilder(objectMapper).build(), municipioService);
    }

    CEPCacheService(RestClient client, MunicipioService municipioService) {
        this.client = client;
        this.municipioService = municipioService;
    }

    static RestClient.Builder restClientBuilder(JsonMapper objectMapper) {
        return RestClient.builder()
                .configureMessageConverters(converters -> converters
                        .withJsonConverter(new JacksonJsonHttpMessageConverter(objectMapper)))
                .defaultHeader(HttpHeaders.USER_AGENT, USER_AGENT)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
    }

    public Optional<CEP> doRequest(String url, String cep) {
        var cepCache = cache.getIfPresent(cep);

        if (null != cepCache) {
            return Optional.of(cepCache);
        }

        try {
            semaphore.acquire();

            var response = client.get()
                    .uri(url.formatted(cep))
                    .retrieve()
                    .body(CepResponse.class);
            response.setCep(response.getCep().replaceAll("\\D", ""));

            var municipio = this.municipioService.findByNome(response.getCity(), response.getState())
                    .orElseThrow(() -> new NotFoundException("município não encontrado"));

            var domain = new CEP(
                    municipio.id(),
                    municipio.estado().id(),
                    response.getCep(),
                    response.getStreet(),
                    response.getNeighborhood(),
                    response.getIbge()
            );

            cache.put(cep, domain);

            return Optional.of(domain);
        } catch (Exception e) {
            return Optional.empty();
        } finally {
            semaphore.release();
        }
    }

}
