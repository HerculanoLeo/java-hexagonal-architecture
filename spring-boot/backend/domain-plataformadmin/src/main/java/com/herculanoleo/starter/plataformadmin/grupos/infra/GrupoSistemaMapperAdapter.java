package com.herculanoleo.starter.plataformadmin.grupos.infra;

import com.herculanoleo.starter.identity.grupos.domain.Grupo;
import com.herculanoleo.starter.identity.grupos.domain.GrupoRegister;
import com.herculanoleo.starter.identity.grupos.domain.GrupoSearch;
import com.herculanoleo.starter.identity.grupos.domain.GrupoUpdate;
import com.herculanoleo.starter.plataformadmin.grupos.app.port.GrupoSistemaMapperPort;
import com.herculanoleo.starter.plataformadmin.grupos.domain.GrupoSistema;
import com.herculanoleo.starter.plataformadmin.grupos.domain.GrupoSistemaRegister;
import com.herculanoleo.starter.plataformadmin.grupos.domain.GrupoSistemaSearch;
import com.herculanoleo.starter.plataformadmin.grupos.domain.GrupoSistemaUpdate;
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
