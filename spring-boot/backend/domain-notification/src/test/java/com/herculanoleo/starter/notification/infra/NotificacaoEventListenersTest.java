package com.herculanoleo.starter.notification.infra;

import com.herculanoleo.starter.notification.app.NotificacaoConfiguracaoService;
import com.herculanoleo.starter.notification.app.NotificacaoService;
import com.herculanoleo.starter.notification.domain.Notificacao;
import com.herculanoleo.starter.notification.domain.NotificacaoConfiguracao;
import com.herculanoleo.starter.notification.domain.NotificacaoRegister;
import com.herculanoleo.starter.notification.domain.events.NotificacaoBoasVindasEvent;
import com.herculanoleo.starter.notification.domain.events.NotificacaoRegisterEvent;
import com.herculanoleo.starter.notification.domain.events.NotificacaoTrocaSenhaEvent;
import com.herculanoleo.starter.shared.models.enums.NotificacaoConfiguracaoCodigo;
import com.herculanoleo.starter.shared.models.enums.NotificacaoStatus;
import com.herculanoleo.starter.shared.models.enums.TipoAcesso;
import com.herculanoleo.starter.shared.models.enums.TipoNotificacao;
import freemarker.template.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import java.time.OffsetDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificacaoEventListenersTest {

    @Mock
    private NotificacaoService service;

    @Mock
    private NotificacaoConfiguracaoService configuracaoService;

    @Mock
    private FreeMarkerConfig freeMarkerConfig;

    @Mock
    private Configuration freemarkerConfiguration;

    @InjectMocks
    @Spy
    private NotificacaoEventListeners eventListeners;

    @BeforeEach
    void setUp() {
        // Common setup for freemarker if needed
        lenient().when(freeMarkerConfig.getConfiguration()).thenReturn(freemarkerConfiguration);
    }

    @Test
    void onNotificacaoRegisterEvent_shouldCallEnviar() {
        // Arrange
        UUID notificacaoId = UUID.randomUUID();
        Notificacao notificacao = new Notificacao(
                notificacaoId,
                "T",
                "C",
                List.of("d"),
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                NotificacaoStatus.PENDENTE,
                TipoNotificacao.EMAIL,
                0,
                0,
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                new HashMap<>(),
                List.of());
        NotificacaoRegisterEvent event = new NotificacaoRegisterEvent(notificacao);

        // Act
        eventListeners.onNotificacaoRegisterEvent(event);

        // Assert
        verify(service).enviar(notificacaoId);
    }

    @Test
    void onNotificacaoRegisterEvent_whenException_shouldLogAndNotThrow() {
        // Arrange
        UUID notificacaoId = UUID.randomUUID();
        Notificacao notificacao = new Notificacao(
                notificacaoId,
                "T",
                "C",
                List.of("d"),
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                NotificacaoStatus.PENDENTE,
                TipoNotificacao.EMAIL,
                0,
                0,
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                new HashMap<>(),
                List.of());
        NotificacaoRegisterEvent event = new NotificacaoRegisterEvent(notificacao);
        doThrow(new RuntimeException("Test Exception")).when(service).enviar(notificacaoId);

        // Act & Assert
        // No exception should be thrown from the listener
        eventListeners.onNotificacaoRegisterEvent(event);
        verify(service).enviar(notificacaoId);
    }

    @Test
    void onNotificacaoBoasVindasEvent_success() {
        // Arrange
        doReturn("Processed Content").when(eventListeners).process(any(), any());

        when(configuracaoService.findByCodigo(NotificacaoConfiguracaoCodigo.BOAS_VINDAS_TITULO))
                .thenReturn(Optional.of(new NotificacaoConfiguracao(UUID.randomUUID(), NotificacaoConfiguracaoCodigo.BOAS_VINDAS_TITULO, "Test Title")));
        when(configuracaoService.findByCodigo(NotificacaoConfiguracaoCodigo.DEFAULT_URL_LINK))
                .thenReturn(Optional.of(new NotificacaoConfiguracao(UUID.randomUUID(), NotificacaoConfiguracaoCodigo.DEFAULT_URL_LINK, "http://test.com")));

        NotificacaoBoasVindasEvent event = new NotificacaoBoasVindasEvent("User", TipoAcesso.USUARIO_SISTEMA, "user@test.com", null, TipoNotificacao.EMAIL);

        // Act
        eventListeners.onNotificacaoBoasVindasEvent(event);

        // Assert
        ArgumentCaptor<NotificacaoRegister> captor = ArgumentCaptor.forClass(NotificacaoRegister.class);
        verify(service).register(captor.capture());

        NotificacaoRegister registered = captor.getValue();
        assertEquals("Test Title", registered.titulo());
        assertEquals("Processed Content", registered.conteudo());
        assertEquals("user@test.com", new ArrayList<>(registered.destinatarios()).getFirst());
        assertEquals(TipoNotificacao.EMAIL, registered.tipo());
    }

    @Test
    void onNotificacaoBoasVindasEvent_whenInvalidType_shouldNotRegister() {
        when(configuracaoService.findByCodigo(NotificacaoConfiguracaoCodigo.BOAS_VINDAS_TITULO))
                .thenReturn(Optional.of(new NotificacaoConfiguracao(UUID.randomUUID(), NotificacaoConfiguracaoCodigo.BOAS_VINDAS_TITULO, "Test Title")));
        when(configuracaoService.findByCodigo(NotificacaoConfiguracaoCodigo.DEFAULT_URL_LINK))
                .thenReturn(Optional.of(new NotificacaoConfiguracao(UUID.randomUUID(), NotificacaoConfiguracaoCodigo.DEFAULT_URL_LINK, "http://test.com")));

        // Arrange
        NotificacaoBoasVindasEvent event = new NotificacaoBoasVindasEvent("User", TipoAcesso.USUARIO_SISTEMA, "pass", "user@test.com", TipoNotificacao.SMS);

        // Act
        eventListeners.onNotificacaoBoasVindasEvent(event);

        // Assert
        verify(service, never()).register(any());
    }

    @Test
    void onNotificacaoBoasVindasEvent_whenNullType_shouldNotRegister() {
        when(configuracaoService.findByCodigo(NotificacaoConfiguracaoCodigo.BOAS_VINDAS_TITULO))
                .thenReturn(Optional.of(new NotificacaoConfiguracao(UUID.randomUUID(), NotificacaoConfiguracaoCodigo.BOAS_VINDAS_TITULO, "Test Title")));
        when(configuracaoService.findByCodigo(NotificacaoConfiguracaoCodigo.DEFAULT_URL_LINK))
                .thenReturn(Optional.of(new NotificacaoConfiguracao(UUID.randomUUID(), NotificacaoConfiguracaoCodigo.DEFAULT_URL_LINK, "http://test.com")));

        // Arrange
        NotificacaoBoasVindasEvent event = new NotificacaoBoasVindasEvent("User", TipoAcesso.USUARIO_SISTEMA, "pass", "user@test.com", null);

        // Act
        eventListeners.onNotificacaoBoasVindasEvent(event);

        // Assert
        verify(service, never()).register(any());
    }

    @Test
    void onNotificacaoTrocaSenhaEvent_success() {
        // Arrange
        doReturn("Processed Content Troca Senha").when(eventListeners).process(any(), any());

        when(configuracaoService.findByCodigo(NotificacaoConfiguracaoCodigo.TROCA_SENHA_TITULO))
                .thenReturn(Optional.of(new NotificacaoConfiguracao(UUID.randomUUID(), NotificacaoConfiguracaoCodigo.TROCA_SENHA_TITULO, "Troca Senha Title")));
        when(configuracaoService.findByCodigo(NotificacaoConfiguracaoCodigo.DEFAULT_URL_LINK))
                .thenReturn(Optional.of(new NotificacaoConfiguracao(UUID.randomUUID(), NotificacaoConfiguracaoCodigo.DEFAULT_URL_LINK, "http://test.com")));

        NotificacaoTrocaSenhaEvent event = new NotificacaoTrocaSenhaEvent("User", TipoAcesso.USUARIO_SISTEMA, "user@test.com", "newpass", TipoNotificacao.EMAIL);

        // Act
        eventListeners.onNotificacaoTrocaSenhaEvent(event);

        // Assert
        ArgumentCaptor<NotificacaoRegister> captor = ArgumentCaptor.forClass(NotificacaoRegister.class);
        verify(service).register(captor.capture());

        NotificacaoRegister registered = captor.getValue();
        assertEquals("Troca Senha Title", registered.titulo());
        assertEquals("Processed Content Troca Senha", registered.conteudo());
        assertEquals("user@test.com", new ArrayList<>(registered.destinatarios()).getFirst());
        assertEquals(TipoNotificacao.EMAIL, registered.tipo());
    }

    @Test
    void onNotificacaoTrocaSenhaEvent_whenMissingConfig_shouldNotRegister() {
        // Arrange
        when(configuracaoService.findByCodigo(NotificacaoConfiguracaoCodigo.TROCA_SENHA_TITULO))
                .thenReturn(Optional.empty()); // Missing config

        NotificacaoTrocaSenhaEvent event = new NotificacaoTrocaSenhaEvent("User", TipoAcesso.USUARIO_SISTEMA, "newpass", "user@test.com", TipoNotificacao.EMAIL);

        // Act
        eventListeners.onNotificacaoTrocaSenhaEvent(event);

        // Assert
        verify(service, never()).register(any());
    }

    @Test
    void onNotificacaoTrocaSenhaEvent_whenInvalidType_shouldNotRegister() {
        when(configuracaoService.findByCodigo(NotificacaoConfiguracaoCodigo.TROCA_SENHA_TITULO))
                .thenReturn(Optional.of(new NotificacaoConfiguracao(UUID.randomUUID(), NotificacaoConfiguracaoCodigo.TROCA_SENHA_TITULO, "Troca Senha Title")));
        when(configuracaoService.findByCodigo(NotificacaoConfiguracaoCodigo.DEFAULT_URL_LINK))
                .thenReturn(Optional.of(new NotificacaoConfiguracao(UUID.randomUUID(), NotificacaoConfiguracaoCodigo.DEFAULT_URL_LINK, "http://test.com")));

        NotificacaoTrocaSenhaEvent event = new NotificacaoTrocaSenhaEvent("User", TipoAcesso.USUARIO_SISTEMA, "user@test.com", "newpass", TipoNotificacao.SMS);

        // Act
        eventListeners.onNotificacaoTrocaSenhaEvent(event);

        // Assert
        verify(service, never()).register(any());
    }

    @Test
    void onNotificacaoTrocaSenhaEvent_whenNullType_shouldNotRegister() {
        when(configuracaoService.findByCodigo(NotificacaoConfiguracaoCodigo.TROCA_SENHA_TITULO))
                .thenReturn(Optional.of(new NotificacaoConfiguracao(UUID.randomUUID(), NotificacaoConfiguracaoCodigo.TROCA_SENHA_TITULO, "Troca Senha Title")));
        when(configuracaoService.findByCodigo(NotificacaoConfiguracaoCodigo.DEFAULT_URL_LINK))
                .thenReturn(Optional.of(new NotificacaoConfiguracao(UUID.randomUUID(), NotificacaoConfiguracaoCodigo.DEFAULT_URL_LINK, "http://test.com")));

        NotificacaoTrocaSenhaEvent event = new NotificacaoTrocaSenhaEvent("User", TipoAcesso.USUARIO_SISTEMA, "user@test.com", "newpass", null);

        // Act
        eventListeners.onNotificacaoTrocaSenhaEvent(event);

        // Assert
        verify(service, never()).register(any());
    }

}
