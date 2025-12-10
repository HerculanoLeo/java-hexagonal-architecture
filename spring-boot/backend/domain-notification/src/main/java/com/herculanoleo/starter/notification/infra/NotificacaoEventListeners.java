package com.herculanoleo.starter.notification.infra;

import com.herculanoleo.starter.notification.app.NotificacaoConfiguracaoService;
import com.herculanoleo.starter.notification.app.NotificacaoService;
import com.herculanoleo.starter.notification.domain.NotificacaoConfiguracao;
import com.herculanoleo.starter.notification.domain.NotificacaoRegister;
import com.herculanoleo.starter.notification.domain.events.NotificacaoBoasVindasEvent;
import com.herculanoleo.starter.notification.domain.events.NotificacaoRegisterEvent;
import com.herculanoleo.starter.notification.domain.events.NotificacaoTrocaSenhaEvent;
import com.herculanoleo.starter.shared.exceptions.BadRequestException;
import com.herculanoleo.starter.shared.models.annotations.ExcludeFromJacocoGeneratedReport;
import com.herculanoleo.starter.shared.models.enums.FreemarkerTemplate;
import com.herculanoleo.starter.shared.models.enums.NotificacaoConfiguracaoCodigo;
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
        try {
            this.service.enviar(event.notificacao().id());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @ApplicationModuleListener
    public void onNotificacaoBoasVindasEvent(NotificacaoBoasVindasEvent event) {
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
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @ApplicationModuleListener
    public void onNotificacaoTrocaSenhaEvent(NotificacaoTrocaSenhaEvent event) {
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
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}
