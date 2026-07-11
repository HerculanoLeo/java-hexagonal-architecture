package com.lodh8.starter.backoffice.grupos.api.dtos;

import com.lodh8.starter.shared.models.enums.TipoAcesso;
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
