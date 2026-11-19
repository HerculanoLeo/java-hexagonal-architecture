package com.lodh8.starter.seguranca.historico.infra.mapper;

import com.lodh8.starter.seguranca.historico.api.dto.HistoricoLoginDTO;
import com.lodh8.starter.seguranca.historico.api.dto.HistoricoLoginSearchDTO;
import com.lodh8.starter.seguranca.historico.domain.HistoricoLogin;
import com.lodh8.starter.seguranca.historico.domain.HistoricoLoginSearch;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface HistoricoLoginDTOMapper {

    HistoricoLoginSearch domain(HistoricoLoginSearchDTO requestEntity);

    HistoricoLoginDTO dto(HistoricoLogin historico);

}
