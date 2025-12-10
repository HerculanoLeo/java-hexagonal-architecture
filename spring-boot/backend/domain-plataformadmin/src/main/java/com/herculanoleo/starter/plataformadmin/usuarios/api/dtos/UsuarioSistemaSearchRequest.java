package com.herculanoleo.starter.plataformadmin.usuarios.api.dtos;

import com.herculanoleo.starter.shared.models.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioSistemaSearchRequest {

    private String nome;

    private String email;

    private Status status;

}
