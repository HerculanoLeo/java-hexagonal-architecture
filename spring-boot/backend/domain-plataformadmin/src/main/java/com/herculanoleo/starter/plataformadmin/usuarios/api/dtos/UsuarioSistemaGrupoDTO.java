package com.herculanoleo.starter.plataformadmin.usuarios.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioSistemaGrupoDTO {

    private UUID id;

    private String nome;

}
