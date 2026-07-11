package com.lodh8.starter.security.historico.app.impl;

import com.lodh8.starter.authorize.app.port.OauthUserProviderPort;
import com.lodh8.starter.backoffice.usuarios.app.UsuarioSistemaService;
import com.lodh8.starter.backoffice.usuarios.domain.UsuarioSistema;
import com.lodh8.starter.security.historico.app.HistoricoLoginService;
import com.lodh8.starter.security.historico.app.port.HistoricoLoginRepositoryPort;
import com.lodh8.starter.security.historico.domain.HistoricoLogin;
import com.lodh8.starter.security.historico.domain.HistoricoLoginRegister;
import com.lodh8.starter.security.historico.domain.HistoricoLoginSearch;
import com.lodh8.starter.shared.models.enums.TipoAcesso;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class HistoricoLoginServiceImpl implements HistoricoLoginService {

    private final HistoricoLoginRepositoryPort repository;

    private final OauthUserProviderPort oauthUserProvider;

    private final UsuarioSistemaService usuarioSistemaService;

    @Override
    public Collection<HistoricoLogin> findAll(HistoricoLoginSearch search) {
        return this.repository.findAll(search);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void register(String ip, String userAgent, String sessaoBffId, String email, String nome) {
        if (StringUtils.isNotBlank(sessaoBffId) && this.repository.existsBySessaoBffId(sessaoBffId)) {
            return;
        }

        var oauthUser = this.oauthUserProvider.oauthUser();
        if (oauthUser == null || StringUtils.isBlank(oauthUser.userId())) {
            log.warn("historico-login: usuário OAuth ausente; registro ignorado");
            return;
        }

        var tipo = oauthUser.type() != null ? oauthUser.type() : TipoAcesso.ANONYMOUS;
        var relacionadoId = oauthUser.details() != null ? oauthUser.details().relacionadoId() : null;

        UUID usuarioId = null;
        if (TipoAcesso.USUARIO_SISTEMA.equals(tipo)) {
            usuarioId = this.usuarioSistemaService.findByIdentityId(oauthUser.userId())
                    .map(UsuarioSistema::id)
                    .orElse(null);
        }

        this.repository.register(new HistoricoLoginRegister(
                oauthUser.userId(),
                usuarioId,
                tipo,
                relacionadoId,
                StringUtils.trimToNull(email),
                StringUtils.trimToNull(nome),
                StringUtils.trimToNull(ip),
                StringUtils.trimToNull(userAgent),
                StringUtils.trimToNull(sessaoBffId)
        ));
    }

}
