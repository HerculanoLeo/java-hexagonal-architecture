package com.lodh8.starter.security.historico.infra;

import com.lodh8.starter.security.historico.domain.HistoricoLogin;
import com.lodh8.starter.security.historico.domain.HistoricoLoginRegister;
import com.lodh8.starter.security.historico.infra.persistence.HistoricoLoginEntity;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface HistoricoLoginMapper {

    @Mapping(target = "identityId", source = "idIdentity")
    @Mapping(target = "usuarioId", source = "idUsuario")
    @Mapping(target = "relacionadoId", source = "idRelacionado")
    @Mapping(target = "sessaoBffId", source = "idSessaoBff")
    HistoricoLogin domain(HistoricoLoginEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "idIdentity", source = "identityId")
    @Mapping(target = "idUsuario", source = "usuarioId")
    @Mapping(target = "idRelacionado", source = "relacionadoId")
    @Mapping(target = "idSessaoBff", source = "sessaoBffId")
    @Mapping(target = "sucesso", ignore = true)
    @Mapping(target = "dataEvento", ignore = true)
    HistoricoLoginEntity entity(HistoricoLoginRegister domain);

    @AfterMapping
    default void mapEntity(@MappingTarget HistoricoLoginEntity entity, HistoricoLoginRegister domain) {
        entity.setSucesso(true);
        if (entity.getDataEvento() == null) {
            entity.setDataEvento(java.time.OffsetDateTime.now());
        }
    }

}
