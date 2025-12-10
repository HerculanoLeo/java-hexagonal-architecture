package com.herculanoleo.starter.notification.infra;

import com.herculanoleo.starter.notification.domain.NotificacaoConfiguracao;
import com.herculanoleo.starter.notification.infra.persistence.NotificacaoConfiguracaoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NotificacaoConfiguracaoMapper {

    NotificacaoConfiguracao domain(NotificacaoConfiguracaoEntity entity);

}
