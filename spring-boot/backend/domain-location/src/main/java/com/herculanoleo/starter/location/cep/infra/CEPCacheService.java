package com.herculanoleo.starter.location.cep.infra;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.herculanoleo.starter.location.cep.domain.CEP;
import com.herculanoleo.starter.location.cep.infra.dtos.CepResponse;
import com.herculanoleo.starter.location.municipio.app.MunicipioService;
import com.herculanoleo.starter.shared.exceptions.NotFoundException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

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

    protected final RestTemplate template;

    private final MunicipioService municipioService;

    public CEPCacheService(ObjectMapper objectMapper, MunicipioService municipioService) {
        this.municipioService = municipioService;
        this.template = new RestTemplate();
        this.template.getMessageConverters().add(new MappingJackson2HttpMessageConverter(objectMapper));
    }

    public Optional<CEP> doRequest(String url, String cep) {
        var cepCache = cache.getIfPresent(cep);

        if (null != cepCache) {
            return Optional.of(cepCache);
        }

        try {
            semaphore.acquire();

            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            headers.set(HttpHeaders.USER_AGENT, USER_AGENT);
            HttpEntity<CepResponse> httpEntity = new HttpEntity<>(headers);

            var response = template.exchange(url.formatted(cep), HttpMethod.GET, httpEntity, CepResponse.class).getBody();
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
