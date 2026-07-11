package com.lodh8.starter.notification.infra.persistence;

import com.lodh8.starter.notification.app.port.NotificacaoRepositoryPort;
import com.lodh8.starter.notification.domain.Notificacao;
import com.lodh8.starter.notification.domain.NotificacaoError;
import com.lodh8.starter.notification.domain.NotificacaoRegister;
import com.lodh8.starter.notification.domain.NotificacaoSearch;
import com.lodh8.starter.notification.infra.NotificacaoMapper;
import com.lodh8.starter.shared.models.enums.NotificacaoStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificacaoRepositoryAdapter implements NotificacaoRepositoryPort {

    private final NotificacaoEntityRepository repository;

    private final NotificacaoMapper mapper;

    @Override
    public Collection<Notificacao> findAll(NotificacaoSearch requestEntity) {
        var entities = this.repository.findAll(NotificacaoEntitySpecification.spec()
                .and(NotificacaoEntitySpecification.status(requestEntity.status()))
                .and(NotificacaoEntitySpecification.tipo(requestEntity.tipo()))
                .and(NotificacaoEntitySpecification.tentativasGTEQ(requestEntity.minTentativas()))
                .and(NotificacaoEntitySpecification.tentativasLTEQ(requestEntity.maxTentativas()))
                .and(NotificacaoEntitySpecification.dataSolicitacaoGTEQ(requestEntity.dataSolicitacaoDe()))
                .and(NotificacaoEntitySpecification.dataSolicitacaoLTEQ(requestEntity.dataSolicitacaoAte()))
        );
        return entities.stream().map(this.mapper::domain).toList();
    }

    @Override
    public Optional<Notificacao> findById(UUID id) {
        return this.repository.findById(id).map(this.mapper::domain);
    }

    @Override
    public Notificacao register(NotificacaoRegister requestEntity) {
        var entity = this.repository.save(this.mapper.entity(requestEntity));
        return this.mapper.domain(entity);
    }

    @Override
    public void enviado(UUID id) {
        var entity = this.repository.findById(id).orElseThrow();
        entity.setStatus(NotificacaoStatus.ENVIADO);
        entity.setDataEnvio(OffsetDateTime.now());
        this.repository.save(entity);
    }

    @Override
    public void erro(UUID id, Integer tentativa, NotificacaoError error) {
        var entity = this.repository.findById(id).orElseThrow();
        entity.setStatus(NotificacaoStatus.ERRO);
        entity.setTentativas(tentativa);
        entity.getErrors().add(error);
        this.repository.save(entity);
    }

}
