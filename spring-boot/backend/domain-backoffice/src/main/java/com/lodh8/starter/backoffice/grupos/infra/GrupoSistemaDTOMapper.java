package com.lodh8.starter.backoffice.grupos.infra;

import com.lodh8.starter.backoffice.grupos.api.dtos.GrupoSistemaDTO;
import com.lodh8.starter.backoffice.grupos.api.dtos.GrupoSistemaRegisterRequest;
import com.lodh8.starter.backoffice.grupos.api.dtos.GrupoSistemaSearchRequest;
import com.lodh8.starter.backoffice.grupos.api.dtos.GrupoSistemaUpdateRequest;
import com.lodh8.starter.backoffice.grupos.domain.GrupoSistema;
import com.lodh8.starter.backoffice.grupos.domain.GrupoSistemaRegister;
import com.lodh8.starter.backoffice.grupos.domain.GrupoSistemaSearch;
import com.lodh8.starter.backoffice.grupos.domain.GrupoSistemaUpdate;
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
