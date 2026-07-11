package com.lodh8.starter.location.cep.app.impl;

import com.lodh8.starter.location.cep.app.port.CepProviderPort;
import com.lodh8.starter.location.cep.domain.CEP;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CepServiceImplTest {

    @Mock
    private CepProviderPort provider1;

    @Mock
    private CepProviderPort provider2;

    private CepServiceImpl service;

    @BeforeEach
    void setUp() {
        // We manually instantiate the service to inject the list of mocks
        service = new CepServiceImpl(List.of(provider1, provider2));
    }

    @Test
    void findByCep_whenFirstProviderFinds_shouldReturnResultAndStop() {
        // Arrange
        String cep = "12345-678";
        var expectedCep = mock(CEP.class);
        when(provider1.findByCep(cep)).thenReturn(Optional.of(expectedCep));

        // Act
        Optional<CEP> result = service.findByCep(cep);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(expectedCep, result.get());
        verify(provider1, times(1)).findByCep(cep);
        verify(provider2, never()).findByCep(any());
    }

    @Test
    void findByCep_whenSecondProviderFinds_shouldReturnResult() {
        // Arrange
        String cep = "12345-678";
        var expectedCep = mock(CEP.class);
        when(provider1.findByCep(cep)).thenReturn(Optional.empty());
        when(provider2.findByCep(cep)).thenReturn(Optional.of(expectedCep));

        // Act
        Optional<CEP> result = service.findByCep(cep);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(expectedCep, result.get());
        verify(provider1, times(1)).findByCep(cep);
        verify(provider2, times(1)).findByCep(cep);
    }

    @Test
    void findByCep_whenNoProviderFinds_shouldReturnEmpty() {
        // Arrange
        String cep = "12345-678";
        when(provider1.findByCep(cep)).thenReturn(Optional.empty());
        when(provider2.findByCep(cep)).thenReturn(Optional.empty());

        // Act
        Optional<CEP> result = service.findByCep(cep);

        // Assert
        assertTrue(result.isEmpty());
        verify(provider1, times(1)).findByCep(cep);
        verify(provider2, times(1)).findByCep(cep);
    }

    @Test
    void findByCep_withBlankCep_shouldReturnEmptyAndNotCallProviders() {
        // Arrange
        String cep = "   ";

        // Act
        Optional<CEP> result = service.findByCep(cep);

        // Assert
        assertTrue(result.isEmpty());
        verify(provider1, never()).findByCep(any());
        verify(provider2, never()).findByCep(any());
    }

    @Test
    void findByCep_withNonNumericCep_shouldReturnEmptyAndNotCallProviders() {
        // Arrange
        String cep = "abc-def";

        // Act
        Optional<CEP> result = service.findByCep(cep);

        // Assert
        assertTrue(result.isEmpty());
        verify(provider1, never()).findByCep(any());
        verify(provider2, never()).findByCep(any());
    }
}
