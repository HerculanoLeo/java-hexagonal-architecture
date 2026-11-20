package com.lodh8.starter.notificacao.infra.mapper;

import com.lodh8.starter.notificacao.domain.NotificacaoConfiguracao;
import com.lodh8.starter.notificacao.infra.persistence.NotificacaoConfiguracaoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NotificacaoConfiguracaoMapper {

    NotificacaoConfiguracao domain(NotificacaoConfiguracaoEntity entity);

}
