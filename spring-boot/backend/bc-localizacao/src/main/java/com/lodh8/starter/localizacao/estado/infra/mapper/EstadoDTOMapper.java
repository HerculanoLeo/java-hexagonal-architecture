package com.lodh8.starter.localizacao.estado.infra.mapper;

import com.lodh8.starter.localizacao.estado.api.dto.EstadoDTO;
import com.lodh8.starter.localizacao.estado.api.dto.EstadoSearchDTO;
import com.lodh8.starter.localizacao.estado.domain.Estado;
import com.lodh8.starter.localizacao.estado.domain.EstadoSearch;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EstadoDTOMapper {

    EstadoDTO dto(Estado domain);

    EstadoSearch domain(EstadoSearchDTO dto);

}
