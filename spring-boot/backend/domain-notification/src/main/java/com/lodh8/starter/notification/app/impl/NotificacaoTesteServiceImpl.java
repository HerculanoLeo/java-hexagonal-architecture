package com.lodh8.starter.notification.app.impl;

import com.lodh8.starter.notification.app.NotificacaoConfiguracaoService;
import com.lodh8.starter.notification.app.NotificacaoService;
import com.lodh8.starter.notification.app.NotificacaoTesteService;
import com.lodh8.starter.notification.app.port.usuario.NotificacaoUsuarioAtual;
import com.lodh8.starter.notification.app.port.usuario.NotificacaoUsuarioAtualPort;
import com.lodh8.starter.notification.domain.NotificacaoConfiguracao;
import com.lodh8.starter.notification.domain.NotificacaoRegister;
import com.lodh8.starter.notification.domain.NotificacaoTesteTipo;
import com.lodh8.starter.shared.exceptions.BadRequestException;
import com.lodh8.starter.shared.models.annotations.ExcludeFromJacocoGeneratedReport;
import com.lodh8.starter.shared.models.enums.FreemarkerTemplate;
import com.lodh8.starter.shared.models.enums.NotificacaoConfiguracaoCodigo;
import com.lodh8.starter.shared.models.enums.TipoNotificacao;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificacaoTesteServiceImpl implements NotificacaoTesteService {

    private static final String SENHA_TEMPORARIA_MOCK = "Temp@123";

    private final NotificacaoService notificacaoService;

    private final NotificacaoConfiguracaoService configuracaoService;

    private final NotificacaoUsuarioAtualPort usuarioAtualPort;

    private final FreeMarkerConfig freeMarkerConfig;

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void enviarEmailTeste() {
        enviarEmailTeste(NotificacaoTesteTipo.TESTE);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void enviarEmailTeste(NotificacaoTesteTipo tipo) {
        var usuario = requireUsuarioAtual();
        var registro = switch (tipo) {
            case TESTE -> buildTeste(usuario);
            case BOAS_VINDAS -> buildBoasVindas(usuario);
            case TROCA_SENHA -> buildTrocaSenha(usuario);
            case USUARIO_ATIVADO -> buildUsuarioAtivado(usuario);
            case USUARIO_INATIVADO -> buildUsuarioInativado(usuario);
        };
        notificacaoService.register(registro);
    }

    private NotificacaoRegister buildTeste(NotificacaoUsuarioAtual usuario) {
        var titulo = titulo(NotificacaoConfiguracaoCodigo.TESTE_TITULO);
        var values = new HashMap<String, Object>();
        values.put("titulo", titulo);
        values.put("nomeUsuario", usuario.nome());
        return register("[TESTE] " + titulo, process(FreemarkerTemplate.TESTE, values), usuario.email());
    }

    private NotificacaoRegister buildBoasVindas(NotificacaoUsuarioAtual usuario) {
        var titulo = titulo(NotificacaoConfiguracaoCodigo.BOAS_VINDAS_TITULO);
        var values = new HashMap<String, Object>();
        values.put("titulo", titulo);
        values.put("nome", usuario.nome());
        values.put("senha", SENHA_TEMPORARIA_MOCK);
        values.put("url", defaultUrl());
        return register("[TESTE] " + titulo, process(FreemarkerTemplate.BOAS_VINDAS, values), usuario.email());
    }

    private NotificacaoRegister buildTrocaSenha(NotificacaoUsuarioAtual usuario) {
        var titulo = titulo(NotificacaoConfiguracaoCodigo.TROCA_SENHA_TITULO);
        var values = new HashMap<String, Object>();
        values.put("titulo", titulo);
        values.put("nome", usuario.nome());
        values.put("senha", SENHA_TEMPORARIA_MOCK);
        values.put("url", defaultUrl());
        return register("[TESTE] " + titulo, process(FreemarkerTemplate.TROCA_SENHA, values), usuario.email());
    }

    private NotificacaoRegister buildUsuarioAtivado(NotificacaoUsuarioAtual usuario) {
        var titulo = titulo(NotificacaoConfiguracaoCodigo.USUARIO_ATIVADO_TITULO);
        var values = new HashMap<String, Object>();
        values.put("titulo", titulo);
        values.put("nome", usuario.nome());
        values.put("url", defaultUrl());
        return register("[TESTE] " + titulo, process(FreemarkerTemplate.USUARIO_ATIVADO, values), usuario.email());
    }

    private NotificacaoRegister buildUsuarioInativado(NotificacaoUsuarioAtual usuario) {
        var titulo = titulo(NotificacaoConfiguracaoCodigo.USUARIO_INATIVADO_TITULO);
        var values = new HashMap<String, Object>();
        values.put("titulo", titulo);
        values.put("nome", usuario.nome());
        return register("[TESTE] " + titulo, process(FreemarkerTemplate.USUARIO_INATIVADO, values), usuario.email());
    }

    private NotificacaoRegister register(String titulo, String conteudo, String email) {
        return new NotificacaoRegister(
                titulo,
                conteudo,
                List.of(email),
                TipoNotificacao.EMAIL,
                new HashMap<>()
        );
    }

    private NotificacaoUsuarioAtual requireUsuarioAtual() {
        var usuario = usuarioAtualPort.usuarioAtual();
        if (usuario == null || StringUtils.isBlank(usuario.email())) {
            throw new BadRequestException("usuário autenticado sem e-mail para envio de teste");
        }
        if (StringUtils.isBlank(usuario.nome())) {
            return new NotificacaoUsuarioAtual("Administrador", usuario.email());
        }
        return usuario;
    }

    private String titulo(NotificacaoConfiguracaoCodigo codigo) {
        return configuracaoService.findByCodigo(codigo)
                .map(NotificacaoConfiguracao::valor)
                .orElseThrow(() -> new BadRequestException("configuração de notificação não encontrada: " + codigo.getValue()));
    }

    private String defaultUrl() {
        return configuracaoService.findByCodigo(NotificacaoConfiguracaoCodigo.DEFAULT_URL_LINK)
                .map(NotificacaoConfiguracao::valor)
                .orElseThrow(() -> new BadRequestException("configuração default_url_link não encontrada"));
    }

    @ExcludeFromJacocoGeneratedReport
    @SneakyThrows
    private String process(FreemarkerTemplate templateEnum, Map<String, Object> values) {
        var template = freeMarkerConfig.getConfiguration().getTemplate(templateEnum.getValue());
        return FreeMarkerTemplateUtils.processTemplateIntoString(template, values);
    }

}
