package com.lodh8.starter.localizacao.cep.infra.mapper;


import com.lodh8.starter.localizacao.cep.api.dto.CEPDTO;
import com.lodh8.starter.localizacao.cep.domain.CEP;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CEPDTOMapper {

    CEPDTO dto(CEP domain);

}
