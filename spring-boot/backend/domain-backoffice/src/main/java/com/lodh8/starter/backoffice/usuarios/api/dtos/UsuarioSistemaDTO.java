package com.lodh8.starter.backoffice.usuarios.api.dtos;

import com.lodh8.starter.shared.models.enums.Status;
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

    /**
     * Usuário principal: persistido em {@code tb_sistema_usuario.main} e sincronizado
     * com a realm role {@code admin-sistemas} no Keycloak.
     */
    private boolean main;

}
