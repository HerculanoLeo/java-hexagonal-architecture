package com.herculanoleo.starter.plataformadmin.usuarios.infra;

import com.herculanoleo.starter.plataformadmin.usuarios.api.dtos.*;
import com.herculanoleo.starter.plataformadmin.usuarios.domain.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UsuarioSistemaDTOMapper {

    @Mapping(target = "identityId", ignore = true)
    @Mapping(target = "withIdentity", ignore = true)
    UsuarioSistemaRegister domain(UsuarioSistemaRegisterRequest requestEntity);

    UsuarioSistemaDTO dto(UsuarioSistema domain);

    UsuarioSistemaGrupoDTO dto(UsuarioSistemaGrupo domain);

    UsuarioSistemaSearch domain(UsuarioSistemaSearchRequest requestEntity);

    UsuarioSistemaUpdate domain(UsuarioSistemaUpdateRequest requestEntity);
}
