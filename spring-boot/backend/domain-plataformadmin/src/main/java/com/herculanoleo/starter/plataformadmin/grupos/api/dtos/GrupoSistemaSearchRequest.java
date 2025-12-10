package com.herculanoleo.starter.plataformadmin.grupos.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GrupoSistemaSearchRequest {

    private String nome;

}
