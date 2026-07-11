package com.lodh8.starter.backoffice.usuarios.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioSistemaUpdateRequest {

    private boolean main;

    private String nome;

    private String grupoId;

}
