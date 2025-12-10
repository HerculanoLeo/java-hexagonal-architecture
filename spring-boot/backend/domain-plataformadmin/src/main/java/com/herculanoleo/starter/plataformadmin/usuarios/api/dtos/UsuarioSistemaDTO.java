package com.herculanoleo.starter.plataformadmin.usuarios.api.dtos;

import com.herculanoleo.starter.shared.models.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioSistemaDTO {

    private UUID id;

    private String email;

    private String nome;

    private Status status;

}
