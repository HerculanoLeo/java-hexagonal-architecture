package com.herculanoleo.starter.location.cep.infra;


import com.herculanoleo.starter.location.cep.api.dto.CEPDTO;
import com.herculanoleo.starter.location.cep.domain.CEP;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CEPDTOMapper {

    CEPDTO dto(CEP domain);

}
