package com.herculanoleo.starter.location.municipio.infra;

import com.herculanoleo.starter.location.estado.infra.EstadoMapper;
import com.herculanoleo.starter.location.municipio.domain.Municipio;
import com.herculanoleo.starter.location.municipio.domain.MunicipioRegister;
import com.herculanoleo.starter.location.municipio.infra.persistence.MunicipioEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {EstadoMapper.class})
public interface MunicipioMapper {

    Municipio domain(MunicipioEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "estado", ignore = true)
    MunicipioEntity entity(MunicipioRegister domain);
}
