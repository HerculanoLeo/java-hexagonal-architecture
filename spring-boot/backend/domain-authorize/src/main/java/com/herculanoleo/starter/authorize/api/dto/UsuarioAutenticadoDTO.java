package com.herculanoleo.starter.authorize.api.dto;

import com.herculanoleo.starter.shared.models.enums.Status;
import com.herculanoleo.starter.shared.models.enums.TipoAcesso;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioAutenticadoDTO {

    private UUID id;

    private String identityId;

    private String relacionadoId;

    private String nome;

    private String email;

    private TipoAcesso tipo;

    private Status status;

    private Integer versao;

}
