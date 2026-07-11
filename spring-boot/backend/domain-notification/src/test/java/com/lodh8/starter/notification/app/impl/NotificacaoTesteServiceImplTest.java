package com.lodh8.starter.notification.app.impl;

import com.lodh8.starter.notification.app.NotificacaoConfiguracaoService;
import com.lodh8.starter.notification.app.NotificacaoService;
import com.lodh8.starter.notification.app.port.usuario.NotificacaoUsuarioAtual;
import com.lodh8.starter.notification.app.port.usuario.NotificacaoUsuarioAtualPort;
import com.lodh8.starter.notification.domain.NotificacaoConfiguracao;
import com.lodh8.starter.notification.domain.NotificacaoRegister;
import com.lodh8.starter.notification.domain.NotificacaoTesteTipo;
import com.lodh8.starter.shared.exceptions.BadRequestException;
import com.lodh8.starter.shared.models.enums.NotificacaoConfiguracaoCodigo;
import com.lodh8.starter.shared.models.enums.TipoNotificacao;
import freemarker.template.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificacaoTesteServiceImplTest {

    @Mock
    private NotificacaoService notificacaoService;

    @Mock
    private NotificacaoConfiguracaoService configuracaoService;

    @Mock
    private NotificacaoUsuarioAtualPort usuarioAtualPort;

    @Mock
    private FreeMarkerConfig freeMarkerConfig;

    @InjectMocks
    private NotificacaoTesteServiceImpl service;

    @BeforeEach
    void setUp() throws Exception {
        var cfg = new Configuration(Configuration.VERSION_2_3_32);
        cfg.setDirectoryForTemplateLoading(Path.of(
                "src/main/resources/freemarker-templates"
        ).toFile());
        cfg.setDefaultEncoding("UTF-8");
        cfg.setRecognizeStandardFileExtensions(true);
        lenient().when(freeMarkerConfig.getConfiguration()).thenReturn(cfg);
    }

    @Test
    void enviarEmailTeste_shouldRegisterTesteTemplate() {
        when(usuarioAtualPort.usuarioAtual()).thenReturn(new NotificacaoUsuarioAtual("Admin", "admin@test.com"));
        when(configuracaoService.findByCodigo(NotificacaoConfiguracaoCodigo.TESTE_TITULO))
                .thenReturn(Optional.of(config(NotificacaoConfiguracaoCodigo.TESTE_TITULO, "[PORTAL] E-mail de teste")));

        service.enviarEmailTeste();

        var captor = ArgumentCaptor.forClass(NotificacaoRegister.class);
        verify(notificacaoService).register(captor.capture());
        var register = captor.getValue();
        assertEquals("[TESTE] [PORTAL] E-mail de teste", register.titulo());
        assertEquals(List.of("admin@test.com"), register.destinatarios().stream().toList());
        assertEquals(TipoNotificacao.EMAIL, register.tipo());
        assertTrue(register.conteudo().contains("Admin"));
        assertTrue(register.conteudo().contains("e-mail de teste"));
    }

    @Test
    void enviarEmailTeste_boasVindas_shouldIncludeMockPasswordAndUrl() {
        when(usuarioAtualPort.usuarioAtual()).thenReturn(new NotificacaoUsuarioAtual("Maria", "maria@test.com"));
        when(configuracaoService.findByCodigo(NotificacaoConfiguracaoCodigo.BOAS_VINDAS_TITULO))
                .thenReturn(Optional.of(config(NotificacaoConfiguracaoCodigo.BOAS_VINDAS_TITULO, "Boas vindas")));
        when(configuracaoService.findByCodigo(NotificacaoConfiguracaoCodigo.DEFAULT_URL_LINK))
                .thenReturn(Optional.of(config(NotificacaoConfiguracaoCodigo.DEFAULT_URL_LINK, "http://localhost")));

        service.enviarEmailTeste(NotificacaoTesteTipo.BOAS_VINDAS);

        var captor = ArgumentCaptor.forClass(NotificacaoRegister.class);
        verify(notificacaoService).register(captor.capture());
        assertEquals("[TESTE] Boas vindas", captor.getValue().titulo());
        assertEquals(List.of("maria@test.com"), captor.getValue().destinatarios().stream().toList());
        assertTrue(captor.getValue().conteudo().contains("Temp@123"));
    }

    @Test
    void enviarEmailTeste_whenEmailBlank_shouldThrow() {
        when(usuarioAtualPort.usuarioAtual()).thenReturn(new NotificacaoUsuarioAtual("Admin", " "));

        assertThrows(BadRequestException.class, () -> service.enviarEmailTeste());
        verify(notificacaoService, never()).register(any());
    }

    private static NotificacaoConfiguracao config(NotificacaoConfiguracaoCodigo codigo, String valor) {
        return new NotificacaoConfiguracao(UUID.randomUUID(), codigo, valor);
    }
}
