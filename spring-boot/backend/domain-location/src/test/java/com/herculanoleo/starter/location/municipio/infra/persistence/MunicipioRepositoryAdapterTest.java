package com.herculanoleo.starter.location.municipio.infra.persistence;

import com.herculanoleo.starter.location.municipio.domain.Municipio;
import com.herculanoleo.starter.location.municipio.domain.MunicipioRegister;
import com.herculanoleo.starter.location.municipio.domain.MunicipioSearch;
import com.herculanoleo.starter.location.municipio.domain.MunicipioUpdate;
import com.herculanoleo.starter.location.municipio.infra.MunicipioMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
@ExtendWith(MockitoExtension.class)
class MunicipioRepositoryAdapterTest {

    @Mock
    private MunicipioEntityRepository repository;

    @Mock
    private MunicipioMapper mapper;

    @InjectMocks
    private MunicipioRepositoryAdapter adapter;

    @Test
    void findAll_shouldCallRepositoryWithSpecificationAndMapResults() {
        // Arrange
        var search = new MunicipioSearch(null, null, null, null);
        var entity = mock(MunicipioEntity.class);
        var domain = mock(Municipio.class);

        when(repository.findAll(any(Specification.class))).thenReturn(Collections.singletonList(entity));
        when(mapper.domain(entity)).thenReturn(domain);

        // Act
        Collection<Municipio> result = adapter.findAll(search);

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(domain, result.iterator().next());
        verify(repository).findAll(any(Specification.class));
    }

    @Test
    void findById_whenFound_shouldMapAndReturnObject() {
        // Arrange
        Long id = 1L;
        var entity = mock(MunicipioEntity.class);
        var domain = mock(Municipio.class);
        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.domain(entity)).thenReturn(domain);

        // Act
        Optional<Municipio> result = adapter.findById(id);

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
        Optional<Municipio> result = adapter.findById(id);

        // Assert
        assertTrue(result.isEmpty());
        verify(mapper, never()).domain(any());
    }

    @Test
    void findByNome_whenFound_shouldMapAndReturnObject() {
        // Arrange
        String nome = "São Paulo";
        String uf = "SP";
        var entity = mock(MunicipioEntity.class);
        var domain = mock(Municipio.class);
        when(repository.findByNome(nome, uf)).thenReturn(Optional.of(entity));
        when(mapper.domain(entity)).thenReturn(domain);

        // Act
        Optional<Municipio> result = adapter.findByNome(nome, uf);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(domain, result.get());
    }

    @Test
    void findByNome_whenNotFound_shouldReturnEmpty() {
        // Arrange
        String nome = "Non Existent";
        String uf = "XX";
        when(repository.findByNome(nome, uf)).thenReturn(Optional.empty());

        // Act
        Optional<Municipio> result = adapter.findByNome(nome, uf);

        // Assert
        assertTrue(result.isEmpty());
        verify(mapper, never()).domain(any());
    }

    @Test
    void register_shouldMapSaveAndReturnMappedDomain() {
        // Arrange
        var register = mock(MunicipioRegister.class);
        var entityToSave = new MunicipioEntity();
        var savedEntity = new MunicipioEntity();
        var domain = mock(Municipio.class);

        when(mapper.entity(register)).thenReturn(entityToSave);
        when(repository.save(entityToSave)).thenReturn(savedEntity);
        when(mapper.domain(savedEntity)).thenReturn(domain);

        // Act
        Municipio result = adapter.register(register);

        // Assert
        assertEquals(domain, result);
        verify(mapper).entity(register);
        verify(repository).save(entityToSave);
        verify(mapper).domain(savedEntity);
    }

    @Test
    void update_whenFound_shouldUpdateFieldsAndSave() {
        // Arrange
        Long id = 1L;
        var update = new MunicipioUpdate("New Name", 2L, null);
        var entity = spy(new MunicipioEntity());

        when(repository.findById(id)).thenReturn(Optional.of(entity));

        // Act
        adapter.update(id, update);

        // Assert
        ArgumentCaptor<MunicipioEntity> captor = ArgumentCaptor.forClass(MunicipioEntity.class);
        verify(repository).save(captor.capture());
        MunicipioEntity savedEntity = captor.getValue();

        assertEquals("New Name", savedEntity.getNome());
        assertEquals(2L, savedEntity.getEstadoId());
        assertNull(savedEntity.getStatus());
    }

    @Test
    void update_whenNotFound_shouldThrowException() {
        // Arrange
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> adapter.update(id, mock(MunicipioUpdate.class)));
        verify(repository, never()).save(any());
    }

    @Test
    void delete_shouldCallRepositoryDelete() {
        // Arrange
        Long id = 1L;

        // Act
        adapter.delete(id);

        // Assert
        verify(repository, times(1)).deleteById(id);
    }
}
