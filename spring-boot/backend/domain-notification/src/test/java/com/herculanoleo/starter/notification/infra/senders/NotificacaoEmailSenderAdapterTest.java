package com.herculanoleo.starter.notification.infra.senders;

import com.herculanoleo.starter.notification.domain.Notificacao;
import com.herculanoleo.starter.notification.infra.attributes.NotificacaoAttributes;
import com.herculanoleo.starter.shared.models.enums.TipoNotificacao;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificacaoEmailSenderAdapterTest {

    @Mock
    private NotificacaoAttributes attributes;

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private NotificacaoEmailSenderAdapter adapter;

    @BeforeEach
    void setUp() {
        // Use reflection to set the "from" field, as it's injected by @Value
        ReflectionTestUtils.setField(adapter, "from", "test@example.com");
    }

    @Test
    void suporta_withEmail_shouldReturnTrue() {
        assertTrue(adapter.suporta(TipoNotificacao.EMAIL));
    }

    @Test
    void suporta_withSms_shouldReturnFalse() {
        assertFalse(adapter.suporta(TipoNotificacao.SMS));
    }

    @Test
    void enviar_whenEnabledAndIsEmail_shouldSendEmail() {
        // Arrange
        NotificacaoAttributes.Email emailAttributes = mock(NotificacaoAttributes.Email.class);
        when(attributes.getEmail()).thenReturn(emailAttributes);
        when(emailAttributes.isEnabled()).thenReturn(true);

        Notificacao notificacao = new Notificacao(
                null, "Test Subject", "Test Content", List.of("recipient@test.com"),
                null, null, null, TipoNotificacao.EMAIL, 0, 0, null, null, null, null
        );

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Act
        adapter.enviar(notificacao);

        // Assert
        verify(mailSender, times(1)).createMimeMessage();
        // We can't easily mock MimeMessageHelper as it's a concrete class new'd up inside the method.
        // However, we can verify the final send call, which is the most important part.
        verify(mailSender, times(1)).send(mimeMessage);
    }

    @Test
    void enviar_whenDisabled_shouldNotSendEmail() {
        // Arrange
        NotificacaoAttributes.Email emailAttributes = mock(NotificacaoAttributes.Email.class);
        when(attributes.getEmail()).thenReturn(emailAttributes);
        when(emailAttributes.isEnabled()).thenReturn(false);

        Notificacao notificacao = new Notificacao(
                null, "Test", "Content", List.of("r@t.com"),
                null, null, null, TipoNotificacao.EMAIL, 0, 0, null, null, null, null
        );

        // Act
        adapter.enviar(notificacao);

        // Assert
        verify(mailSender, never()).createMimeMessage();
        verify(mailSender, never()).send(any(MimeMessage.class));
    }

    @Test
    void enviar_whenNotEmailType_shouldNotSendEmail() {
        // Arrange
        NotificacaoAttributes.Email emailAttributes = mock(NotificacaoAttributes.Email.class);
        when(attributes.getEmail()).thenReturn(emailAttributes);
        when(emailAttributes.isEnabled()).thenReturn(true);

        Notificacao notificacao = new Notificacao(
                null, "Test", "Content", List.of("r@t.com"),
                null, null, null, TipoNotificacao.SMS, 0, 0, null, null, null, null
        );

        // Act
        adapter.enviar(notificacao);

        // Assert
        verify(mailSender, never()).createMimeMessage();
        verify(mailSender, never()).send(any(MimeMessage.class));
    }

    @Test
    void enviar_whenError_shouldThrowException() {
        // Arrange
        NotificacaoAttributes.Email emailAttributes = mock(NotificacaoAttributes.Email.class);
        when(attributes.getEmail()).thenReturn(emailAttributes);
        when(emailAttributes.isEnabled()).thenReturn(true);

        Notificacao notificacao = new Notificacao(
                null, "Test Subject", "Test Content", List.of("recipient@test.com"),
                null, null, null, TipoNotificacao.EMAIL, 0, 0, null, null, null, null
        );

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doThrow(RuntimeException.class).when(mailSender).send(any(MimeMessage.class));

        // Act
        assertThrows(RuntimeException.class, () -> adapter.enviar(notificacao));
    }

}
