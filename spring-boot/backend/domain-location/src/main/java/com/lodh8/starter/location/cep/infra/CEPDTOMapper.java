package com.lodh8.starter.location.cep.infra;


import com.lodh8.starter.location.cep.api.dto.CEPDTO;
import com.lodh8.starter.location.cep.domain.CEP;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CEPDTOMapper {

    CEPDTO dto(CEP domain);

}
