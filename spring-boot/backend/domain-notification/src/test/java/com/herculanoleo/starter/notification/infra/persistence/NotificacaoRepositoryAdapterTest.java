package com.herculanoleo.starter.notification.infra.persistence;

import com.herculanoleo.starter.notification.domain.Notificacao;
import com.herculanoleo.starter.notification.domain.NotificacaoError;
import com.herculanoleo.starter.notification.domain.NotificacaoRegister;
import com.herculanoleo.starter.notification.domain.NotificacaoSearch;
import com.herculanoleo.starter.notification.infra.NotificacaoMapper;
import com.herculanoleo.starter.shared.models.enums.NotificacaoStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
@ExtendWith(MockitoExtension.class)
class NotificacaoRepositoryAdapterTest {

    @Mock
    private NotificacaoEntityRepository repository;

    @Mock
    private NotificacaoMapper mapper;

    @InjectMocks
    private NotificacaoRepositoryAdapter adapter;

    @Test
    void findAll_shouldCallRepositoryWithSpecificationAndMapResults() {
        // Arrange
        var search = new NotificacaoSearch(null, null, null, null, null, null);
        var entity = mock(NotificacaoEntity.class);
        var domain = mock(Notificacao.class);
        when(repository.findAll(any(Specification.class))).thenReturn(List.of(entity));
        when(mapper.domain(entity)).thenReturn(domain);

        // Act
        Collection<Notificacao> result = adapter.findAll(search);

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
        UUID id = UUID.randomUUID();
        var entity = mock(NotificacaoEntity.class);
        var domain = mock(Notificacao.class);
        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.domain(entity)).thenReturn(domain);

        // Act
        Optional<Notificacao> result = adapter.findById(id);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(domain, result.get());
    }

    @Test
    void findById_whenNotFound_shouldReturnEmpty() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        // Act
        Optional<Notificacao> result = adapter.findById(id);

        // Assert
        assertTrue(result.isEmpty());
        verify(mapper, never()).domain(any());
    }

    @Test
    void register_shouldMapSaveAndReturnMappedDomain() {
        // Arrange
        var register = new NotificacaoRegister("t", "c", null, null, null);
        var entityToSave = new NotificacaoEntity();
        var savedEntity = new NotificacaoEntity();
        var domain = mock(Notificacao.class);

        when(mapper.entity(register)).thenReturn(entityToSave);
        when(repository.save(entityToSave)).thenReturn(savedEntity);
        when(mapper.domain(savedEntity)).thenReturn(domain);

        // Act
        Notificacao result = adapter.register(register);

        // Assert
        assertEquals(domain, result);
        verify(mapper).entity(register);
        verify(repository).save(entityToSave);
        verify(mapper).domain(savedEntity);
    }

    @Test
    void enviado_whenFound_shouldUpdateStatusAndDataEnvio() {
        // Arrange
        UUID id = UUID.randomUUID();
        var entity = spy(new NotificacaoEntity());
        when(repository.findById(id)).thenReturn(Optional.of(entity));
        OffsetDateTime fixedNow = OffsetDateTime.of(2023, 1, 1, 12, 0, 0, 0, ZoneOffset.UTC);

        try (MockedStatic<OffsetDateTime> mockedStatic = mockStatic(OffsetDateTime.class)) {
            mockedStatic.when(OffsetDateTime::now).thenReturn(fixedNow);

            // Act
            adapter.enviado(id);

            // Assert
            ArgumentCaptor<NotificacaoEntity> captor = ArgumentCaptor.forClass(NotificacaoEntity.class);
            verify(repository).save(captor.capture());
            NotificacaoEntity savedEntity = captor.getValue();

            assertEquals(NotificacaoStatus.ENVIADO, savedEntity.getStatus());
            assertEquals(fixedNow, savedEntity.getDataEnvio());
        }
    }

    @Test
    void enviado_whenNotFound_shouldThrowException() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> adapter.enviado(id));
        verify(repository, never()).save(any());
    }

    @Test
    void erro_whenFound_shouldUpdateStatusTentativasAndErrors() {
        // Arrange
        UUID id = UUID.randomUUID();
        var entity = spy(new NotificacaoEntity());
        entity.setErrors(new ArrayList<>()); // Initialize the list
        when(repository.findById(id)).thenReturn(Optional.of(entity));

        int newTentativa = 2;
        var error = new NotificacaoError("code", OffsetDateTime.now());

        // Act
        adapter.erro(id, newTentativa, error);

        // Assert
        ArgumentCaptor<NotificacaoEntity> captor = ArgumentCaptor.forClass(NotificacaoEntity.class);
        verify(repository).save(captor.capture());
        NotificacaoEntity savedEntity = captor.getValue();

        assertEquals(NotificacaoStatus.ERRO, savedEntity.getStatus());
        assertEquals(newTentativa, savedEntity.getTentativas());
        assertTrue(savedEntity.getErrors().contains(error));
        assertEquals(1, savedEntity.getErrors().size());
    }

    @Test
    void erro_whenNotFound_shouldThrowException() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> adapter.erro(id, 1, null));
        verify(repository, never()).save(any());
    }
}
