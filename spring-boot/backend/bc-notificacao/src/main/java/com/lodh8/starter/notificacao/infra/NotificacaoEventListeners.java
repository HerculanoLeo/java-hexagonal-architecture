package com.lodh8.starter.notificacao.infra;

import com.lodh8.starter.notificacao.app.NotificacaoConfiguracaoService;
import com.lodh8.starter.notificacao.app.NotificacaoService;
import com.lodh8.starter.notificacao.domain.NotificacaoConfiguracao;
import com.lodh8.starter.notificacao.domain.NotificacaoRegister;
import com.lodh8.starter.notificacao.domain.events.*;
import com.lodh8.starter.shared.exceptions.BadRequestException;
import com.lodh8.starter.shared.models.annotations.ExcludeFromJacocoGeneratedReport;
import com.lodh8.starter.shared.models.enums.FreemarkerTemplate;
import com.lodh8.starter.shared.models.enums.NotificacaoConfiguracaoCodigo;
import com.lodh8.starter.shared.utils.IdentificadorLog;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificacaoEventListeners {

    private final NotificacaoService service;

    private final NotificacaoConfiguracaoService configuracaoService;

    private final FreeMarkerConfig freeMarkerConfig;

    @ExcludeFromJacocoGeneratedReport
    @SneakyThrows
    public String process(FreemarkerTemplate templateEnum, Object values) {
        var template = freeMarkerConfig.getConfiguration().getTemplate(templateEnum.getValue());
        return FreeMarkerTemplateUtils.processTemplateIntoString(template, values);
    }

    @ApplicationModuleListener
    public void onNotificacaoRegisterEvent(NotificacaoRegisterEvent event) {
        var id = event.notificacao().id();
        log.info("notificacaoListener register inicio id={}", IdentificadorLog.mascarar(id));
        log.debug("notificacaoListener register inicio id={}", id);
        try {
            this.service.enviar(id);
            log.info("notificacaoListener register fim id={}", IdentificadorLog.mascarar(id));
        } catch (Exception e) {
            log.error("notificacaoListener register id={} erro", IdentificadorLog.mascarar(id), e);
        }
    }

    @ApplicationModuleListener
    public void onNotificacaoBoasVindasEvent(NotificacaoBoasVindasEvent event) {
        log.info(
                "notificacaoListener boasVindas inicio destinatario={}",
                IdentificadorLog.mascarar(event.destinatario())
        );
        try {
            var titulo = configuracaoService.findByCodigo(NotificacaoConfiguracaoCodigo.BOAS_VINDAS_TITULO)
                    .map(NotificacaoConfiguracao::valor)
                    .orElseThrow();

            var defaultUrlLink = configuracaoService.findByCodigo(NotificacaoConfiguracaoCodigo.DEFAULT_URL_LINK)
                    .map(NotificacaoConfiguracao::valor)
                    .orElseThrow();

            var conteudo = switch (event.tipoNotificacao()) {
                case EMAIL -> {
                    var values = new HashMap<String, Object>();
                    values.put("titulo", titulo);
                    values.put("nome", event.nome());
                    values.put("senha", event.senha());
                    values.put("url", defaultUrlLink);
                    values.put("tipoAcesso", event.tipoAcesso().getValue());
                    yield process(FreemarkerTemplate.BOAS_VINDAS, values);
                }
                case null, default ->
                        throw new BadRequestException("tipo de notificação invalida para evento de boas vindas");
            };

            this.service.register(new NotificacaoRegister(
                    titulo,
                    conteudo,
                    List.of(event.destinatario()),
                    event.tipoNotificacao(),
                    new HashMap<>()
            ));
            log.info("notificacaoListener boasVindas fim");
        } catch (Exception e) {
            log.error("notificacaoListener boasVindas erro", e);
        }
    }

    @ApplicationModuleListener
    public void onNotificacaoTrocaSenhaEvent(NotificacaoTrocaSenhaEvent event) {
        log.info(
                "notificacaoListener trocaSenha inicio destinatario={}",
                IdentificadorLog.mascarar(event.destinatario())
        );
        try {
            var titulo = configuracaoService.findByCodigo(NotificacaoConfiguracaoCodigo.TROCA_SENHA_TITULO)
                    .map(NotificacaoConfiguracao::valor)
                    .orElseThrow();

            var defaultUrlLink = configuracaoService.findByCodigo(NotificacaoConfiguracaoCodigo.DEFAULT_URL_LINK)
                    .map(NotificacaoConfiguracao::valor)
                    .orElseThrow();

            var conteudo = switch (event.tipoNotificacao()) {
                case EMAIL -> {
                    var values = new HashMap<String, Object>();
                    values.put("titulo", titulo);
                    values.put("nome", event.nome());
                    values.put("senha", event.senha());
                    values.put("url", defaultUrlLink);
                    values.put("tipoAcesso", event.tipoAcesso().getValue());
                    yield process(FreemarkerTemplate.TROCA_SENHA, values);
                }
                case null, default ->
                        throw new BadRequestException("tipo de notificação invalida para evento de troca de senha");
            };

            this.service.register(new NotificacaoRegister(
                    titulo,
                    conteudo,
                    List.of(event.destinatario()),
                    event.tipoNotificacao(),
                    new HashMap<>()
            ));
            log.info("notificacaoListener trocaSenha fim");
        } catch (Exception e) {
            log.error("notificacaoListener trocaSenha erro", e);
        }
    }

    @ApplicationModuleListener
    public void onNotificacaoUsuarioAtivadoEvent(NotificacaoUsuarioAtivadoEvent event) {
        log.info(
                "notificacaoListener usuarioAtivado inicio destinatario={}",
                IdentificadorLog.mascarar(event.destinatario())
        );
        try {
            var titulo = configuracaoService.findByCodigo(NotificacaoConfiguracaoCodigo.USUARIO_ATIVADO_TITULO)
                    .map(NotificacaoConfiguracao::valor)
                    .orElseThrow();

            var defaultUrlLink = configuracaoService.findByCodigo(NotificacaoConfiguracaoCodigo.DEFAULT_URL_LINK)
                    .map(NotificacaoConfiguracao::valor)
                    .orElseThrow();

            var conteudo = switch (event.tipoNotificacao()) {
                case EMAIL -> {
                    var values = new HashMap<String, Object>();
                    values.put("titulo", titulo);
                    values.put("nome", event.nome());
                    values.put("url", defaultUrlLink);
                    values.put("tipoAcesso", event.tipoAcesso().getValue());
                    yield process(FreemarkerTemplate.USUARIO_ATIVADO, values);
                }
                case null, default ->
                        throw new BadRequestException("tipo de notificação invalida para evento de usuário ativado");
            };

            this.service.register(new NotificacaoRegister(
                    titulo,
                    conteudo,
                    List.of(event.destinatario()),
                    event.tipoNotificacao(),
                    new HashMap<>()
            ));
            log.info("notificacaoListener usuarioAtivado fim");
        } catch (Exception e) {
            log.error("notificacaoListener usuarioAtivado erro", e);
        }
    }

    @ApplicationModuleListener
    public void onNotificacaoUsuarioInativadoEvent(NotificacaoUsuarioInativadoEvent event) {
        log.info(
                "notificacaoListener usuarioInativado inicio destinatario={}",
                IdentificadorLog.mascarar(event.destinatario())
        );
        try {
            var titulo = configuracaoService.findByCodigo(NotificacaoConfiguracaoCodigo.USUARIO_INATIVADO_TITULO)
                    .map(NotificacaoConfiguracao::valor)
                    .orElseThrow();

            var conteudo = switch (event.tipoNotificacao()) {
                case EMAIL -> {
                    var values = new HashMap<String, Object>();
                    values.put("titulo", titulo);
                    values.put("nome", event.nome());
                    values.put("tipoAcesso", event.tipoAcesso().getValue());
                    yield process(FreemarkerTemplate.USUARIO_INATIVADO, values);
                }
                case null, default ->
                        throw new BadRequestException("tipo de notificação invalida para evento de usuário inativado");
            };

            this.service.register(new NotificacaoRegister(
                    titulo,
                    conteudo,
                    List.of(event.destinatario()),
                    event.tipoNotificacao(),
                    new HashMap<>()
            ));
            log.info("notificacaoListener usuarioInativado fim");
        } catch (Exception e) {
            log.error("notificacaoListener usuarioInativado erro", e);
        }
    }

}
