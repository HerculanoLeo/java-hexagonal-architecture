package com.lodh8.starter.notificacao.infra.mapper;

import com.lodh8.starter.notificacao.api.dto.NotificacaoDTO;
import com.lodh8.starter.notificacao.api.dto.NotificacaoSearchDTO;
import com.lodh8.starter.notificacao.domain.Notificacao;
import com.lodh8.starter.notificacao.domain.NotificacaoSearch;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NotificacaoDTOMapper {
    NotificacaoSearch domain(NotificacaoSearchDTO requestEntity);

    NotificacaoDTO dto(Notificacao notificacao);
}
