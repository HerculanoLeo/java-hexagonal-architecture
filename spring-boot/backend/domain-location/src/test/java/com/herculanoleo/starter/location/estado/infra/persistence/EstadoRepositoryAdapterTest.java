package com.herculanoleo.starter.location.estado.infra.persistence;

import com.herculanoleo.starter.location.estado.domain.Estado;
import com.herculanoleo.starter.location.estado.domain.EstadoSearch;
import com.herculanoleo.starter.location.estado.infra.EstadoMapper;
import com.herculanoleo.starter.shared.models.enums.EstadoSigla;
import com.herculanoleo.starter.shared.models.enums.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
@ExtendWith(MockitoExtension.class)
class EstadoRepositoryAdapterTest {

    @Mock
    private EstadoEntityRepository repository;

    @Mock
    private EstadoMapper mapper;

    @InjectMocks
    private EstadoRepositoryAdapter adapter;

    @Test
    void findAll_shouldCallRepositoryWithSpecificationAndMapResults() {
        // Arrange
        var search = new EstadoSearch("Test", Status.ATIVO);
        var entity = mock(EstadoEntity.class);
        var domain = mock(Estado.class);

        when(repository.findAll(any(Specification.class))).thenReturn(Collections.singletonList(entity));
        when(mapper.domain(entity)).thenReturn(domain);

        // Act
        Collection<Estado> result = adapter.findAll(search);

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(domain, result.iterator().next());
        verify(repository).findAll(any(Specification.class));
        verify(mapper).domain(entity);
    }

    @Test
    void findById_whenFound_shouldMapAndReturnObject() {
        // Arrange
        Long id = 1L;
        var entity = mock(EstadoEntity.class);
        var domain = mock(Estado.class);
        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.domain(entity)).thenReturn(domain);

        // Act
        Optional<Estado> result = adapter.findById(id);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(domain, result.get());
    }

    @Test
    void findById_whenNotFound_shouldReturnEmpty() {
        // Arrange
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        // Act
        Optional<Estado> result = adapter.findById(id);

        // Assert
        assertTrue(result.isEmpty());
        verify(mapper, never()).domain(any());
    }

    @Test
    void findBySigla_whenFound_shouldMapAndReturnObject() {
        // Arrange
        var sigla = EstadoSigla.SAO_PAULO;
        var entity = mock(EstadoEntity.class);
        var domain = mock(Estado.class);
        when(repository.findOne(any(Specification.class))).thenReturn(Optional.of(entity));
        when(mapper.domain(entity)).thenReturn(domain);

        // Act
        Optional<Estado> result = adapter.findBySigla(sigla);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(domain, result.get());
    }

    @Test
    void findBySigla_whenNotFound_shouldReturnEmpty() {
        // Arrange
        var sigla = EstadoSigla.SAO_PAULO;
        when(repository.findOne(any(Specification.class))).thenReturn(Optional.empty());

        // Act
        Optional<Estado> result = adapter.findBySigla(sigla);

        // Assert
        assertTrue(result.isEmpty());
        verify(mapper, never()).domain(any());
    }
}
