package com.lodh8.starter.authorize.infra;

import com.lodh8.starter.authorize.api.dto.GrupoAutenticadoDTO;
import com.lodh8.starter.authorize.api.dto.UsuarioAutenticadoDTO;
import com.lodh8.starter.authorize.domain.GrupoAutenticado;
import com.lodh8.starter.authorize.domain.UsuarioAutenticado;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UsuarioAutenticadoDTOMapper {
    UsuarioAutenticadoDTO dto(UsuarioAutenticado domain);

    GrupoAutenticadoDTO dto(GrupoAutenticado domain);
}
