package com.herculanoleo.starter.authorize.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GrupoAutenticadoDTO {

    private String id;

    private String nome;

    Collection<String> roles;

}
