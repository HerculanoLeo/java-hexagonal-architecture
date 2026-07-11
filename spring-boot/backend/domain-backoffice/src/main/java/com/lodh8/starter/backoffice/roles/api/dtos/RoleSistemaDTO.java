package com.lodh8.starter.backoffice.roles.api.dtos;

import com.lodh8.starter.shared.models.enums.TipoAcesso;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleSistemaDTO {

    private final TipoAcesso tipo = TipoAcesso.USUARIO_SISTEMA;

    private String id;

    private String nome;

    private String descricao;

}
