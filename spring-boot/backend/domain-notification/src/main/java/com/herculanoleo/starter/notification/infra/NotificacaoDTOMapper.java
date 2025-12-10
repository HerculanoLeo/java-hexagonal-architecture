package com.herculanoleo.starter.notification.infra;

import com.herculanoleo.starter.notification.api.dto.NotificacaoDTO;
import com.herculanoleo.starter.notification.api.dto.NotificacaoSearchDTO;
import com.herculanoleo.starter.notification.domain.Notificacao;
import com.herculanoleo.starter.notification.domain.NotificacaoSearch;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NotificacaoDTOMapper {
    NotificacaoSearch domain(NotificacaoSearchDTO requestEntity);

    NotificacaoDTO dto(Notificacao notificacao);
}
