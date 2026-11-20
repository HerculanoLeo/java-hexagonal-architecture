package com.lodh8.starter.plataforma.grupos.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GrupoSistemaRegisterRequest {
    private String nome;
    private Collection<String> roles;
}
