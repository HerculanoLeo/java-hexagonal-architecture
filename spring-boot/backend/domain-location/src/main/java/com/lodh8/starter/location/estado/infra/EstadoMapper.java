package com.lodh8.starter.location.estado.infra;

import com.lodh8.starter.location.estado.domain.Estado;
import com.lodh8.starter.location.estado.infra.persistence.EstadoEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EstadoMapper {

    Estado domain(EstadoEntity entity);

}
