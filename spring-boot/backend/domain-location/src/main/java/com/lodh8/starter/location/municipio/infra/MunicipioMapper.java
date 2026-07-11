package com.lodh8.starter.location.municipio.infra;

import com.lodh8.starter.location.estado.infra.EstadoMapper;
import com.lodh8.starter.location.municipio.domain.Municipio;
import com.lodh8.starter.location.municipio.domain.MunicipioRegister;
import com.lodh8.starter.location.municipio.infra.persistence.MunicipioEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {EstadoMapper.class})
public interface MunicipioMapper {

    Municipio domain(MunicipioEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "estado", ignore = true)
    MunicipioEntity entity(MunicipioRegister domain);
}
