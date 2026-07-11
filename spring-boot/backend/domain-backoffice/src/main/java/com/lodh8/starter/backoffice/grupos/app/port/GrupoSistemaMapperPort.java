package com.lodh8.starter.backoffice.grupos.app.port;

import com.lodh8.starter.backoffice.grupos.domain.GrupoSistema;
import com.lodh8.starter.backoffice.grupos.domain.GrupoSistemaRegister;
import com.lodh8.starter.backoffice.grupos.domain.GrupoSistemaSearch;
import com.lodh8.starter.backoffice.grupos.domain.GrupoSistemaUpdate;
import com.lodh8.starter.identity.grupos.domain.Grupo;
import com.lodh8.starter.identity.grupos.domain.GrupoRegister;
import com.lodh8.starter.identity.grupos.domain.GrupoSearch;
import com.lodh8.starter.identity.grupos.domain.GrupoUpdate;
import com.lodh8.starter.shared.models.enums.TipoAcesso;

public interface GrupoSistemaMapperPort {
    GrupoSistema domain(Grupo entity);

    GrupoRegister register(GrupoSistemaRegister requestEntity);

    GrupoUpdate update(GrupoSistemaUpdate requestEntity);

    GrupoSearch search(GrupoSistemaSearch requestEntity);

    default TipoAcesso tipo() {
        return TipoAcesso.USUARIO_SISTEMA;
    }
}
