package com.lodh8.starter.localizacao.cep.infra;

import com.github.benmanes.caffeine.cache.Cache;
import com.lodh8.starter.localizacao.cep.domain.CEP;
import com.lodh8.starter.localizacao.estado.domain.Estado;
import com.lodh8.starter.localizacao.municipio.app.MunicipioService;
import com.lodh8.starter.localizacao.municipio.domain.Municipio;
import com.lodh8.starter.shared.models.enums.EstadoSigla;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.json.JsonMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@ExtendWith(MockitoExtension.class)
class CEPCacheServiceTest {

    @Mock
    private MunicipioService municipioService;

    private CEPCacheService service;

    private MockRestServiceServer mockServer;

    private Cache<String, CEP> cache;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        RestClient.Builder builder = CEPCacheService.restClientBuilder(JsonMapper.builder().build());
        mockServer = MockRestServiceServer.bindTo(builder).build();
        service = new CEPCacheService(builder.build(), municipioService);
        cache = (Cache<String, CEP>) ReflectionTestUtils.getField(service, "cache");
        cache.invalidateAll();
    }

    @Test
    void doRequest_whenCepIsInCache_shouldReturnCachedValue() {
        String cep = "12345-000";
        var cachedCep = mock(CEP.class);
        cache.put(cep, cachedCep);

        Optional<CEP> result = service.doRequest("http://dummy.url/%s", cep);

        assertTrue(result.isPresent());
        assertSame(cachedCep, result.get());
        mockServer.verify();
    }

    @Test
    void doRequest_whenNotInCacheAndSuccess_shouldFetchAndCache() {
        String cep = "12345-000";
        String url = "http://dummy.url/%s";

        mockServer.expect(requestTo("http://dummy.url/12345-000"))
                .andRespond(withSuccess("""
                        {
                          "cep": "12345-000",
                          "city": "Test City",
                          "state": "SP",
                          "street": "Test Street",
                          "neighborhood": "Test Neighborhood",
                          "ibge": "12345"
                        }
                        """, MediaType.APPLICATION_JSON));

        var mockEstado = new Estado(1L, EstadoSigla.SAO_PAULO, "Test State", null);
        var mockMunicipio = new Municipio(10L, "Test City", mockEstado, null);
        when(municipioService.findByNome("Test City", "SP")).thenReturn(Optional.of(mockMunicipio));

        Optional<CEP> result = service.doRequest(url, cep);

        assertTrue(result.isPresent());
        assertEquals("12345000", result.get().cep());
        assertEquals(10L, result.get().municipioId());
        assertNotNull(cache.getIfPresent(cep));
        assertEquals(result.get(), cache.getIfPresent(cep));
        mockServer.verify();
    }

    @Test
    void doRequest_whenApiFails_shouldReturnEmpty() {
        String cep = "12345-000";
        String url = "http://dummy.url/%s";

        mockServer.expect(requestTo("http://dummy.url/12345-000"))
                .andRespond(withServerError());

        Optional<CEP> result = service.doRequest(url, cep);

        assertTrue(result.isEmpty());
        verify(municipioService, never()).findByNome(anyString(), anyString());
        assertNull(cache.getIfPresent(cep));
        mockServer.verify();
    }

    @Test
    void doRequest_whenMunicipioNotFound_shouldReturnEmpty() {
        String cep = "99999-000";
        String url = "http://dummy.url/%s";

        mockServer.expect(requestTo("http://dummy.url/99999-000"))
                .andRespond(withSuccess("""
                        {
                          "cep": "99999-000",
                          "city": "Cidade Fantasma",
                          "state": "XX"
                        }
                        """, MediaType.APPLICATION_JSON));

        when(municipioService.findByNome("Cidade Fantasma", "XX")).thenReturn(Optional.empty());

        Optional<CEP> result = service.doRequest(url, cep);

        assertTrue(result.isEmpty());
        assertNull(cache.getIfPresent(cep));
        verify(municipioService, times(1)).findByNome("Cidade Fantasma", "XX");
        mockServer.verify();
    }
}
