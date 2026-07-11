package com.lodh8.starter.notification.app.impl;

import com.lodh8.starter.notification.app.port.NotificacaoConfiguracaoRepositoryPort;
import com.lodh8.starter.notification.domain.NotificacaoConfiguracao;
import com.lodh8.starter.shared.models.enums.NotificacaoConfiguracaoCodigo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificacaoConfiguracaoServiceImplTest {

    @Mock
    private NotificacaoConfiguracaoRepositoryPort repository;

    @InjectMocks
    private NotificacaoConfiguracaoServiceImpl service;

    @Test
    void findByCodigo_shouldCallRepositoryAndReturnResult() {
        // Arrange
        var codigo = NotificacaoConfiguracaoCodigo.BOAS_VINDAS_TITULO;
        var expectedResult = Optional.of(mock(NotificacaoConfiguracao.class));
        when(repository.findByCodigo(codigo)).thenReturn(expectedResult);

        // Act
        Optional<NotificacaoConfiguracao> actualResult = service.findByCodigo(codigo);

        // Assert
        assertEquals(expectedResult, actualResult);
        verify(repository, times(1)).findByCodigo(codigo);
        verifyNoMoreInteractions(repository);
    }
}
