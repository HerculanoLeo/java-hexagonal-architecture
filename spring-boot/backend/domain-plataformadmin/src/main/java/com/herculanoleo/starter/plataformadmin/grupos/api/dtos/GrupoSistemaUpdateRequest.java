package com.herculanoleo.starter.plataformadmin.grupos.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GrupoSistemaUpdateRequest {
    private String nome;
    private Collection<String> roles;
}
