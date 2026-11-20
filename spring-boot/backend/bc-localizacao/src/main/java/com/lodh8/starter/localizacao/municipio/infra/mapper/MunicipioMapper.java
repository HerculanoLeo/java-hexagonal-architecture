package com.lodh8.starter.localizacao.municipio.infra.mapper;

import com.lodh8.starter.localizacao.estado.infra.mapper.EstadoMapper;
import com.lodh8.starter.localizacao.municipio.domain.Municipio;
import com.lodh8.starter.localizacao.municipio.domain.MunicipioRegister;
import com.lodh8.starter.localizacao.municipio.infra.persistence.MunicipioEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {EstadoMapper.class})
public interface MunicipioMapper {

    Municipio domain(MunicipioEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "estado", ignore = true)
    MunicipioEntity entity(MunicipioRegister domain);
}
