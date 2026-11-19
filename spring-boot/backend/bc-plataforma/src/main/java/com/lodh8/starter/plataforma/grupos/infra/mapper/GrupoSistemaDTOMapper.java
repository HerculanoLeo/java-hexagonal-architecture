package com.lodh8.starter.plataforma.grupos.infra.mapper;

import com.lodh8.starter.plataforma.grupos.api.dtos.GrupoSistemaDTO;
import com.lodh8.starter.plataforma.grupos.api.dtos.GrupoSistemaRegisterRequest;
import com.lodh8.starter.plataforma.grupos.api.dtos.GrupoSistemaSearchRequest;
import com.lodh8.starter.plataforma.grupos.api.dtos.GrupoSistemaUpdateRequest;
import com.lodh8.starter.plataforma.grupos.domain.GrupoSistema;
import com.lodh8.starter.plataforma.grupos.domain.GrupoSistemaRegister;
import com.lodh8.starter.plataforma.grupos.domain.GrupoSistemaSearch;
import com.lodh8.starter.plataforma.grupos.domain.GrupoSistemaUpdate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface GrupoSistemaDTOMapper {

    GrupoSistemaDTO dto(GrupoSistema entity);

    GrupoSistemaSearch search(GrupoSistemaSearchRequest requestEntity);

    @Mapping(target = "nome", source = "nome")
    @Mapping(target = "roles", source = "roles")
    GrupoSistemaRegister register(GrupoSistemaRegisterRequest requestEntity);

    @Mapping(target = "nome", source = "nome")
    @Mapping(target = "roles", source = "roles")
    GrupoSistemaUpdate update(GrupoSistemaUpdateRequest requestEntity);

}
