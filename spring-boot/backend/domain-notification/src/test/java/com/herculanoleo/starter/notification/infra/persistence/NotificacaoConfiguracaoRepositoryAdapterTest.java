package com.herculanoleo.starter.notification.infra.persistence;

import com.herculanoleo.starter.notification.domain.NotificacaoConfiguracao;
import com.herculanoleo.starter.notification.infra.NotificacaoConfiguracaoMapper;
import com.herculanoleo.starter.shared.models.enums.NotificacaoConfiguracaoCodigo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
@ExtendWith(MockitoExtension.class)
class NotificacaoConfiguracaoRepositoryAdapterTest {

    @Mock
    private NotificacaoConfiguracaoEntityRepository repository;

    @Mock
    private NotificacaoConfiguracaoMapper mapper;

    @InjectMocks
    private NotificacaoConfiguracaoRepositoryAdapter adapter;

    @Test
    void findByCodigo_whenFound_shouldMapAndReturnObject() {
        // Arrange
        var codigo = NotificacaoConfiguracaoCodigo.BOAS_VINDAS_TITULO;
        var entity = mock(NotificacaoConfiguracaoEntity.class);
        var domain = mock(NotificacaoConfiguracao.class);

        when(repository.findOne(any(Specification.class))).thenReturn(Optional.of(entity));
        when(mapper.domain(entity)).thenReturn(domain);

        // Act
        Optional<NotificacaoConfiguracao> result = adapter.findByCodigo(codigo);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(domain, result.get());
        verify(repository, times(1)).findOne(any(Specification.class));
        verify(mapper, times(1)).domain(entity);
    }

    @Test
    void findByCodigo_whenNotFound_shouldReturnEmptyOptional() {
        // Arrange
        var codigo = NotificacaoConfiguracaoCodigo.BOAS_VINDAS_TITULO;
        when(repository.findOne(any(Specification.class))).thenReturn(Optional.empty());

        // Act
        Optional<NotificacaoConfiguracao> result = adapter.findByCodigo(codigo);

        // Assert
        assertTrue(result.isEmpty());
        verify(repository, times(1)).findOne(any(Specification.class));
        verify(mapper, never()).domain(any());
    }
}
