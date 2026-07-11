package com.lodh8.starter.backoffice.grupos.infra;

import com.lodh8.starter.backoffice.grupos.app.port.GrupoSistemaMapperPort;
import com.lodh8.starter.backoffice.grupos.domain.GrupoSistema;
import com.lodh8.starter.backoffice.grupos.domain.GrupoSistemaRegister;
import com.lodh8.starter.backoffice.grupos.domain.GrupoSistemaSearch;
import com.lodh8.starter.backoffice.grupos.domain.GrupoSistemaUpdate;
import com.lodh8.starter.identity.grupos.domain.Grupo;
import com.lodh8.starter.identity.grupos.domain.GrupoRegister;
import com.lodh8.starter.identity.grupos.domain.GrupoSearch;
import com.lodh8.starter.identity.grupos.domain.GrupoUpdate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface GrupoSistemaMapperAdapter extends GrupoSistemaMapperPort {

    GrupoSistema domain(Grupo entity);

    @Mapping(target = "relacionadoId", ignore = true)
    @Mapping(target = "tipo", expression = "java(tipo())")
    @Mapping(target = "nome", source = "nome")
    @Mapping(target = "roles", source = "roles")
    GrupoRegister register(GrupoSistemaRegister requestEntity);

    @Mapping(target = "nome", source = "nome")
    @Mapping(target = "roles", source = "roles")
    @Mapping(target = "nomeInterno", ignore = true)
    GrupoUpdate update(GrupoSistemaUpdate requestEntity);

    @Mapping(target = "tipo", expression = "java(tipo())")
    @Mapping(target = "relacionadoId", ignore = true)
    GrupoSearch search(GrupoSistemaSearch requestEntity);

}
