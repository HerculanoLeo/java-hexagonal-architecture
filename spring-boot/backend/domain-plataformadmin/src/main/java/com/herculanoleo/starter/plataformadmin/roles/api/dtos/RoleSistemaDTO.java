package com.herculanoleo.starter.plataformadmin.roles.api.dtos;

import com.herculanoleo.starter.shared.models.enums.TipoAcesso;
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

}
