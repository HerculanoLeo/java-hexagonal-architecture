package com.lodh8.starter.notificacao.infra.persistence;

import com.lodh8.starter.notificacao.app.port.NotificacaoConfiguracaoRepositoryPort;
import com.lodh8.starter.notificacao.domain.NotificacaoConfiguracao;
import com.lodh8.starter.notificacao.infra.mapper.NotificacaoConfiguracaoMapper;
import com.lodh8.starter.shared.models.enums.NotificacaoConfiguracaoCodigo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.lodh8.starter.notificacao.infra.persistence.NotificacaoConfiguracaoEntitySpecification.codigo;

@Service
@RequiredArgsConstructor
public class NotificacaoConfiguracaoRepositoryAdapter implements NotificacaoConfiguracaoRepositoryPort {

    private final NotificacaoConfiguracaoEntityRepository repository;

    private final NotificacaoConfiguracaoMapper mapper;

    @Override
    public Optional<NotificacaoConfiguracao> findByCodigo(NotificacaoConfiguracaoCodigo codigo) {
        return repository.findOne(codigo(codigo)).map(mapper::domain);
    }

}
