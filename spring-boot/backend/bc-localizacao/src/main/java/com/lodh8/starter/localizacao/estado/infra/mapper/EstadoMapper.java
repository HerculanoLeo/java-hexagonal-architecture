package com.lodh8.starter.localizacao.estado.infra.mapper;

import com.lodh8.starter.localizacao.estado.domain.Estado;
import com.lodh8.starter.localizacao.estado.infra.persistence.EstadoEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EstadoMapper {

    Estado domain(EstadoEntity entity);

}
