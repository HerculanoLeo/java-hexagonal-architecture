package com.lodh8.starter.backoffice.usuarios.api.dtos;

import com.lodh8.starter.shared.models.enums.Status;
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
