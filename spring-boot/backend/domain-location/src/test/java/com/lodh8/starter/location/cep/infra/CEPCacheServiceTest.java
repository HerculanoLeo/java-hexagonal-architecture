package com.lodh8.starter.location.cep.infra;

import com.github.benmanes.caffeine.cache.Cache;
import com.lodh8.starter.location.cep.domain.CEP;
import com.lodh8.starter.location.cep.infra.dtos.CepResponse;
import com.lodh8.starter.location.estado.domain.Estado;
import com.lodh8.starter.location.municipio.app.MunicipioService;
import com.lodh8.starter.location.municipio.domain.Municipio;
import com.lodh8.starter.shared.models.enums.EstadoSigla;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.json.JsonMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CEPCacheServiceTest {

    @Mock
    private MunicipioService municipioService;

    @Mock
    private RestTemplate restTemplate;

    private CEPCacheService service;

    private Cache<String, CEP> cache;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        // We need a real service instance to access its real cache
        service = new CEPCacheService(JsonMapper.builder().build(), municipioService);
        // Replace the real RestTemplate with our mock
        ReflectionTestUtils.setField(service, "template", restTemplate);
        // Get a reference to the real cache inside the service
        cache = (Cache<String, CEP>) ReflectionTestUtils.getField(service, "cache");
        cache.invalidateAll(); // Ensure cache is clean before each test
    }

    @Test
    void doRequest_whenCepIsInCache_shouldReturnCachedValue() {
        // Arrange
        String cep = "12345-000";
        var cachedCep = mock(CEP.class);
        cache.put(cep, cachedCep); // Manually put the item in the cache

        // Act
        Optional<CEP> result = service.doRequest("http://dummy.url/%s", cep);

        // Assert
        assertTrue(result.isPresent());
        assertSame(cachedCep, result.get());
        // Verify no external calls were made
        verify(restTemplate, never()).exchange(anyString(), any(), any(), (Class<Object>) any());
    }

    @Test
    void doRequest_whenNotInCacheAndSuccess_shouldFetchAndCache() {
        // Arrange
        String cep = "12345-000";
        String url = "http://dummy.url/%s";

        var cepResponse = new CepResponse();
        cepResponse.setCep("12345-000");
        cepResponse.setCity("Test City");
        cepResponse.setState("SP");
        cepResponse.setStreet("Test Street");
        cepResponse.setNeighborhood("Test Neighborhood");
        cepResponse.setIbge("12345");

        var mockEstado = new Estado(1L, EstadoSigla.SAO_PAULO, "Test State", null);
        var mockMunicipio = new Municipio(10L, "Test City", mockEstado, null);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(CepResponse.class)))
                .thenReturn(ResponseEntity.ok(cepResponse));
        when(municipioService.findByNome("Test City", "SP")).thenReturn(Optional.of(mockMunicipio));

        // Act
        Optional<CEP> result = service.doRequest(url, cep);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("12345000", result.get().cep());
        assertEquals(10L, result.get().municipioId());

        // Verify that the result was cached
        assertNotNull(cache.getIfPresent(cep));
        assertEquals(result.get(), cache.getIfPresent(cep));
    }

    @Test
    void doRequest_whenApiFails_shouldReturnEmpty() {
        // Arrange
        String cep = "12345-000";
        String url = "http://dummy.url/%s";

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(CepResponse.class)))
                .thenThrow(new RuntimeException("API is down"));

        // Act
        Optional<CEP> result = service.doRequest(url, cep);

        // Assert
        assertTrue(result.isEmpty());
        verify(municipioService, never()).findByNome(anyString(), anyString());
        assertNull(cache.getIfPresent(cep)); // Verify nothing was cached
    }

    @Test
    void doRequest_whenMunicipioNotFound_shouldReturnEmpty() {
        // Arrange
        String cep = "99999-000";
        String url = "http://dummy.url/%s";

        // 1. API call is successful
        var cepResponse = new CepResponse();
        cepResponse.setCep(cep);
        cepResponse.setCity("Cidade Fantasma");
        cepResponse.setState("XX");
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(CepResponse.class)))
                .thenReturn(ResponseEntity.ok(cepResponse));

        // 2. Municipio lookup fails
        when(municipioService.findByNome("Cidade Fantasma", "XX")).thenReturn(Optional.empty());

        // Act
        Optional<CEP> result = service.doRequest(url, cep);

        // Assert
        // 3. The final result should be empty because the exception is caught
        assertTrue(result.isEmpty());

        // 4. Verify that no value was put into the cache
        assertNull(cache.getIfPresent(cep));

        // Verify the flow
        verify(restTemplate, times(1)).exchange(anyString(), any(), any(), eq(CepResponse.class));
        verify(municipioService, times(1)).findByNome("Cidade Fantasma", "XX");
    }
}
