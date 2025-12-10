package com.herculanoleo.starter.location.estado.app.impl;

import com.herculanoleo.starter.location.estado.app.ports.EstadoRepositoryPort;
import com.herculanoleo.starter.location.estado.domain.Estado;
import com.herculanoleo.starter.location.estado.domain.EstadoSearch;
import com.herculanoleo.starter.shared.models.enums.EstadoSigla;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EstadoServiceImplTest {

    @Mock
    private EstadoRepositoryPort repository;

    @InjectMocks
    private EstadoServiceImpl service;

    @Test
    void findAll_shouldDelegateToRepository() {
        // Arrange
        var search = mock(EstadoSearch.class);
        var expected = Collections.singletonList(mock(Estado.class));
        when(repository.findAll(search)).thenReturn(expected);

        // Act
        Collection<Estado> result = service.findAll(search);

        // Assert
        assertEquals(expected, result);
        verify(repository, times(1)).findAll(search);
    }

    @Test
    void findById_shouldDelegateToRepository() {
        // Arrange
        Long id = 1L;
        var expected = Optional.of(mock(Estado.class));
        when(repository.findById(id)).thenReturn(expected);

        // Act
        Optional<Estado> result = service.findById(id);

        // Assert
        assertEquals(expected, result);
        verify(repository, times(1)).findById(id);
    }

    @Test
    void findBySigla_shouldDelegateToRepository() {
        // Arrange
        var sigla = EstadoSigla.SAO_PAULO;
        var expected = Optional.of(mock(Estado.class));
        when(repository.findBySigla(sigla)).thenReturn(expected);

        // Act
        Optional<Estado> result = service.findBySigla(sigla);

        // Assert
        assertEquals(expected, result);
        verify(repository, times(1)).findBySigla(sigla);
    }
}
