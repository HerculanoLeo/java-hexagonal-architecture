package com.herculanoleo.starter.plataformadmin.grupos.api.dtos;

import com.herculanoleo.starter.shared.models.enums.TipoAcesso;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GrupoSistemaDTO {

    private final TipoAcesso tipo = TipoAcesso.USUARIO_SISTEMA;

    private String id;

    private String nome;

    private Collection<String> roles;

}
