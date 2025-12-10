package com.herculanoleo.starter.location.estado.infra;

import com.herculanoleo.starter.location.estado.api.dto.EstadoDTO;
import com.herculanoleo.starter.location.estado.api.dto.EstadoSearchDTO;
import com.herculanoleo.starter.location.estado.domain.Estado;
import com.herculanoleo.starter.location.estado.domain.EstadoSearch;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EstadoDTOMapper {

    EstadoDTO dto(Estado domain);

    EstadoSearch domain(EstadoSearchDTO dto);

}
