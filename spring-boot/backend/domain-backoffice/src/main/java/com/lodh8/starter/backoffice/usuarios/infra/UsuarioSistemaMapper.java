package com.lodh8.starter.backoffice.usuarios.infra;

import com.lodh8.starter.backoffice.usuarios.domain.UsuarioSistema;
import com.lodh8.starter.backoffice.usuarios.domain.UsuarioSistemaRegister;
import com.lodh8.starter.backoffice.usuarios.infra.persistence.UsuarioSistemaEntity;
import com.lodh8.starter.identity.usuario.domain.UsuarioRegister;
import com.lodh8.starter.shared.models.enums.Status;
import com.lodh8.starter.shared.models.enums.TipoAcesso;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UsuarioSistemaMapper {

    UsuarioSistema domain(UsuarioSistemaEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "identityId", source = "identityId")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "nome", source = "nome")
    @Mapping(target = "main", source = "main")
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "versao", ignore = true)
    UsuarioSistemaEntity entity(UsuarioSistemaRegister domain);

    @AfterMapping
    default void mapEntity(@MappingTarget UsuarioSistemaEntity entity, UsuarioSistemaRegister domain) {
        entity.setStatus(Status.ATIVO);
    }

    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "emailVerified", ignore = true)
    @Mapping(target = "tipo", expression = "java(tipo())")
    @Mapping(target = "relacionadoId", ignore = true)
    @Mapping(target = "redirectAction", ignore = true)
    UsuarioRegister domain(UsuarioSistemaRegister requestEntity);

    default TipoAcesso tipo() {
        return TipoAcesso.USUARIO_SISTEMA;
    }

}
