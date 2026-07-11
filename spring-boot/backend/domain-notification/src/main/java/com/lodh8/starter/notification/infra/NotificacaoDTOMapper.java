package com.lodh8.starter.notification.infra;

import com.lodh8.starter.notification.api.dto.NotificacaoDTO;
import com.lodh8.starter.notification.api.dto.NotificacaoSearchDTO;
import com.lodh8.starter.notification.domain.Notificacao;
import com.lodh8.starter.notification.domain.NotificacaoSearch;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NotificacaoDTOMapper {
    NotificacaoSearch domain(NotificacaoSearchDTO requestEntity);

    NotificacaoDTO dto(Notificacao notificacao);
}
