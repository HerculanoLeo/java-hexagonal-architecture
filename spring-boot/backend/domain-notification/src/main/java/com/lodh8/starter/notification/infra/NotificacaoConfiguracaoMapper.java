package com.lodh8.starter.notification.infra;

import com.lodh8.starter.notification.domain.NotificacaoConfiguracao;
import com.lodh8.starter.notification.infra.persistence.NotificacaoConfiguracaoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NotificacaoConfiguracaoMapper {

    NotificacaoConfiguracao domain(NotificacaoConfiguracaoEntity entity);

}
