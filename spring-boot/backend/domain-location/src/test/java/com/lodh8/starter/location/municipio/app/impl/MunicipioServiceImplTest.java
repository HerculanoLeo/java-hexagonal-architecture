package com.lodh8.starter.location.municipio.app.impl;

import com.lodh8.starter.location.estado.app.EstadoService;
import com.lodh8.starter.location.estado.domain.Estado;
import com.lodh8.starter.location.municipio.app.port.MunicipioRepositoryPort;
import com.lodh8.starter.location.municipio.domain.Municipio;
import com.lodh8.starter.location.municipio.domain.MunicipioRegister;
import com.lodh8.starter.location.municipio.domain.MunicipioSearch;
import com.lodh8.starter.location.municipio.domain.MunicipioUpdate;
import com.lodh8.starter.shared.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MunicipioServiceImplTest {

    @Mock
    private MunicipioRepositoryPort repository;

    @Mock
    private EstadoService estadoService;

    @InjectMocks
    private MunicipioServiceImpl service;

    @Test
    void findAll_shouldDelegateToRepository() {
        // Arrange
        var search = mock(MunicipioSearch.class);
        var expected = Collections.singletonList(mock(Municipio.class));
        when(repository.findAll(search)).thenReturn(expected);

        // Act
        Collection<Municipio> result = service.findAll(search);

        // Assert
        assertEquals(expected, result);
        verify(repository, times(1)).findAll(search);
    }

    @Test
    void findById_shouldDelegateToRepository() {
        // Arrange
        Long id = 1L;
        var expected = Optional.of(mock(Municipio.class));
        when(repository.findById(id)).thenReturn(expected);

        // Act
        Optional<Municipio> result = service.findById(id);

        // Assert
        assertEquals(expected, result);
        verify(repository, times(1)).findById(id);
    }

    @Test
    void findByNome_shouldDelegateToRepository() {
        // Arrange
        String nome = "São Paulo";
        String uf = "SP";
        var expected = Optional.of(mock(Municipio.class));
        when(repository.findByNome(nome, uf)).thenReturn(expected);

        // Act
        Optional<Municipio> result = service.findByNome(nome, uf);

        // Assert
        assertEquals(expected, result);
        verify(repository, times(1)).findByNome(nome, uf);
    }

    @Test
    void register_whenEstadoExists_shouldCallRepository() {
        // Arrange
        var register = new MunicipioRegister("Test City", 1L, null);
        when(estadoService.findById(1L)).thenReturn(Optional.of(mock(Estado.class)));

        // Act
        service.register(register);

        // Assert
        verify(repository, times(1)).register(register);
    }

    @Test
    void register_whenEstadoDoesNotExist_shouldThrowNotFoundException() {
        // Arrange
        var register = new MunicipioRegister("Test City", 1L, null);
        when(estadoService.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> service.register(register));
        verify(repository, never()).register(any());
    }

    @Test
    void update_whenAllExist_shouldCallRepository() {
        // Arrange
        Long municipioId = 10L;
        var update = new MunicipioUpdate("New Name", 2L, null);
        when(repository.findById(municipioId)).thenReturn(Optional.of(mock(Municipio.class)));
        when(estadoService.findById(2L)).thenReturn(Optional.of(mock(Estado.class)));

        // Act
        service.update(municipioId, update);

        // Assert
        verify(repository, times(1)).update(municipioId, update);
    }

    @Test
    void update_whenMunicipioDoesNotExist_shouldThrowException() {
        // Arrange
        Long municipioId = 10L;
        var update = new MunicipioUpdate("New Name", 2L, null);
        when(repository.findById(municipioId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> service.update(municipioId, update));
        verify(repository, never()).update(any(), any());
    }

    @Test
    void update_whenEstadoDoesNotExist_shouldThrowException() {
        // Arrange
        Long municipioId = 10L;
        var update = new MunicipioUpdate("New Name", 2L, null);
        when(repository.findById(municipioId)).thenReturn(Optional.of(mock(Municipio.class)));
        when(estadoService.findById(2L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> service.update(municipioId, update));
        verify(repository, never()).update(any(), any());
    }

    @Test
    void delete_whenMunicipioExists_shouldCallRepository() {
        // Arrange
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.of(mock(Municipio.class)));

        // Act
        service.delete(id);

        // Assert
        verify(repository, times(1)).delete(id);
    }

    @Test
    void delete_whenMunicipioDoesNotExist_shouldThrowException() {
        // Arrange
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> service.delete(id));
        verify(repository, never()).delete(any());
    }
}
