package com.lodh8.starter.notification.infra;

import com.lodh8.starter.notification.app.NotificacaoService;
import com.lodh8.starter.notification.infra.attributes.NotificacaoAttributes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificacaoReenvioJobTest {

    @Mock
    private NotificacaoAttributes attributes;

    @Mock
    private NotificacaoService service;

    @InjectMocks
    private NotificacaoReenvioJob job;

    @Test
    void run_shouldCallReenvioWithCorrectDateRangeAndTentativas() {
        // Arrange
        OffsetDateTime fixedNow = OffsetDateTime.of(2023, 10, 27, 10, 0, 0, 0, ZoneOffset.UTC);
        int maxTentativas = 5;

        NotificacaoAttributes.Email emailAttributes = mock(NotificacaoAttributes.Email.class);
        when(attributes.getEmail()).thenReturn(emailAttributes);
        when(emailAttributes.getMaxTentativas()).thenReturn(maxTentativas);

        // Mocking static OffsetDateTime.now()
        try (MockedStatic<OffsetDateTime> mockedStatic = mockStatic(OffsetDateTime.class)) {
            mockedStatic.when(OffsetDateTime::now).thenReturn(fixedNow);

            // Act
            job.run();

            // Assert
            ArgumentCaptor<OffsetDateTime> dataDeCaptor = ArgumentCaptor.forClass(OffsetDateTime.class);
            ArgumentCaptor<OffsetDateTime> dataAteCaptor = ArgumentCaptor.forClass(OffsetDateTime.class);
            ArgumentCaptor<Integer> maxTentativasCaptor = ArgumentCaptor.forClass(Integer.class);

            verify(service).reenvio(dataDeCaptor.capture(), dataAteCaptor.capture(), maxTentativasCaptor.capture());

            assertEquals(fixedNow.minusDays(1), dataDeCaptor.getValue());
            assertEquals(fixedNow.minusHours(1), dataAteCaptor.getValue());
            assertEquals(maxTentativas, maxTentativasCaptor.getValue());
        }
    }
}
