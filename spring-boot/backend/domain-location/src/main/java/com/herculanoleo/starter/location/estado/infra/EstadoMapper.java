package com.herculanoleo.starter.location.estado.infra;

import com.herculanoleo.starter.location.estado.domain.Estado;
import com.herculanoleo.starter.location.estado.infra.persistence.EstadoEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EstadoMapper {

    Estado domain(EstadoEntity entity);

}
