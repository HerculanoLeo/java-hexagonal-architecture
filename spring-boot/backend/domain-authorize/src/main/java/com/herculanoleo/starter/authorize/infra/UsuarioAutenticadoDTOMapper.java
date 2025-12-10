package com.herculanoleo.starter.authorize.infra;

import com.herculanoleo.starter.authorize.api.dto.GrupoAutenticadoDTO;
import com.herculanoleo.starter.authorize.api.dto.UsuarioAutenticadoDTO;
import com.herculanoleo.starter.authorize.domain.GrupoAutenticado;
import com.herculanoleo.starter.authorize.domain.UsuarioAutenticado;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UsuarioAutenticadoDTOMapper {
    UsuarioAutenticadoDTO dto(UsuarioAutenticado domain);

    GrupoAutenticadoDTO dto(GrupoAutenticado domain);
}
