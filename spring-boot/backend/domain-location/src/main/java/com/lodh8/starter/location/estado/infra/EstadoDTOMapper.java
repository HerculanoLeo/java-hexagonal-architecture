package com.lodh8.starter.location.estado.infra;

import com.lodh8.starter.location.estado.api.dto.EstadoDTO;
import com.lodh8.starter.location.estado.api.dto.EstadoSearchDTO;
import com.lodh8.starter.location.estado.domain.Estado;
import com.lodh8.starter.location.estado.domain.EstadoSearch;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EstadoDTOMapper {

    EstadoDTO dto(Estado domain);

    EstadoSearch domain(EstadoSearchDTO dto);

}
