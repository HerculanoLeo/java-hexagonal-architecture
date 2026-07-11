package com.lodh8.starter.notification.infra;

import com.lodh8.starter.notification.domain.Notificacao;
import com.lodh8.starter.notification.domain.NotificacaoRegister;
import com.lodh8.starter.notification.infra.persistence.NotificacaoEntity;
import com.lodh8.starter.shared.models.enums.NotificacaoStatus;
import org.mapstruct.*;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NotificacaoMapper {

    Notificacao domain(NotificacaoEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataSolicitacao", ignore = true)
    @Mapping(target = "dataEnvio", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "tentativas", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "errors", ignore = true)
    NotificacaoEntity entity(NotificacaoRegister domain);

    @AfterMapping
    default void mapEntity(@MappingTarget NotificacaoEntity entity, NotificacaoRegister domain) {
        entity.setDataSolicitacao(OffsetDateTime.now());
        entity.setStatus(NotificacaoStatus.PENDENTE);
        entity.setTentativas(0);
        entity.setErrors(Collections.emptyList());
    }


    default String destinatarios(Collection<String> destinatarios) {
        return String.join(";", destinatarios);
    }

    default Collection<String> destinatarios(String destinatarios) {
        return Set.of(destinatarios.split(";"));
    }

}
