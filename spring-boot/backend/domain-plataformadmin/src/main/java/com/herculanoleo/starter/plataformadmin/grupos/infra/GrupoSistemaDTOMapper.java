package com.herculanoleo.starter.plataformadmin.grupos.infra;

import com.herculanoleo.starter.plataformadmin.grupos.api.dtos.GrupoSistemaDTO;
import com.herculanoleo.starter.plataformadmin.grupos.api.dtos.GrupoSistemaRegisterRequest;
import com.herculanoleo.starter.plataformadmin.grupos.api.dtos.GrupoSistemaSearchRequest;
import com.herculanoleo.starter.plataformadmin.grupos.api.dtos.GrupoSistemaUpdateRequest;
import com.herculanoleo.starter.plataformadmin.grupos.domain.GrupoSistema;
import com.herculanoleo.starter.plataformadmin.grupos.domain.GrupoSistemaRegister;
import com.herculanoleo.starter.plataformadmin.grupos.domain.GrupoSistemaSearch;
import com.herculanoleo.starter.plataformadmin.grupos.domain.GrupoSistemaUpdate;
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
