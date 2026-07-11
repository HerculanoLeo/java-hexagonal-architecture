package com.lodh8.starter.location.municipio.infra;

import com.lodh8.starter.location.estado.infra.EstadoDTOMapper;
import com.lodh8.starter.location.municipio.api.dto.MunicipioDTO;
import com.lodh8.starter.location.municipio.api.dto.MunicipioRegisterDTO;
import com.lodh8.starter.location.municipio.api.dto.MunicipioSearchDTO;
import com.lodh8.starter.location.municipio.api.dto.MunicipioUpdateDTO;
import com.lodh8.starter.location.municipio.domain.Municipio;
import com.lodh8.starter.location.municipio.domain.MunicipioRegister;
import com.lodh8.starter.location.municipio.domain.MunicipioSearch;
import com.lodh8.starter.location.municipio.domain.MunicipioUpdate;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {EstadoDTOMapper.class})
public interface MunicipioDTOMapper {

    MunicipioDTO dto(Municipio domain);

    MunicipioSearch domain(MunicipioSearchDTO dto);

    MunicipioRegister domain(MunicipioRegisterDTO dto);

    MunicipioUpdate domain(MunicipioUpdateDTO dto);
}
