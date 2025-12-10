package com.herculanoleo.starter.plataformadmin.grupos.app.port;

import com.herculanoleo.starter.identity.grupos.domain.Grupo;
import com.herculanoleo.starter.identity.grupos.domain.GrupoRegister;
import com.herculanoleo.starter.identity.grupos.domain.GrupoSearch;
import com.herculanoleo.starter.identity.grupos.domain.GrupoUpdate;
import com.herculanoleo.starter.plataformadmin.grupos.domain.GrupoSistema;
import com.herculanoleo.starter.plataformadmin.grupos.domain.GrupoSistemaRegister;
import com.herculanoleo.starter.plataformadmin.grupos.domain.GrupoSistemaSearch;
import com.herculanoleo.starter.plataformadmin.grupos.domain.GrupoSistemaUpdate;
import com.herculanoleo.starter.shared.models.enums.TipoAcesso;

public interface GrupoSistemaMapperPort {
    GrupoSistema domain(Grupo entity);

    GrupoRegister register(GrupoSistemaRegister requestEntity);

    GrupoUpdate update(GrupoSistemaUpdate requestEntity);

    GrupoSearch search(GrupoSistemaSearch requestEntity);

    default TipoAcesso tipo() {
        return TipoAcesso.USUARIO_SISTEMA;
    }
}
