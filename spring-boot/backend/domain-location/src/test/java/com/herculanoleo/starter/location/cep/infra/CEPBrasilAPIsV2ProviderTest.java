package com.herculanoleo.starter.location.cep.infra;

import com.herculanoleo.starter.location.cep.domain.CEP;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CEPBrasilAPIsV2ProviderTest {

    @Mock
    private CEPCacheService cacheService;

    @InjectMocks
    private CEPBrasilAPIsV2Provider provider;

    @Test
    void findByCep_shouldDelegateToCacheServiceWithCorrectUrl() {
        // Arrange
        String cep = "12345-678";
        var expectedResult = Optional.of(mock(CEP.class));

        when(cacheService.doRequest(CEPBrasilAPIsV2Provider.URL, cep)).thenReturn(expectedResult);

        // Act
        Optional<CEP> actualResult = provider.findByCep(cep);

        // Assert
        assertEquals(expectedResult, actualResult);
        verify(cacheService, times(1)).doRequest(CEPBrasilAPIsV2Provider.URL, cep);
        verifyNoMoreInteractions(cacheService);
    }
}
