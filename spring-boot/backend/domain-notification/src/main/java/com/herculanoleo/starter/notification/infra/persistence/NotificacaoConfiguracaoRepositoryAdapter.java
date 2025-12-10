package com.herculanoleo.starter.notification.infra.persistence;

import com.herculanoleo.starter.notification.app.ports.NotificacaoConfiguracaoRepositoryPort;
import com.herculanoleo.starter.notification.domain.NotificacaoConfiguracao;
import com.herculanoleo.starter.notification.infra.NotificacaoConfiguracaoMapper;
import com.herculanoleo.starter.shared.models.enums.NotificacaoConfiguracaoCodigo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.herculanoleo.starter.notification.infra.persistence.NotificacaoConfiguracaoEntitySpecification.codigo;

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
