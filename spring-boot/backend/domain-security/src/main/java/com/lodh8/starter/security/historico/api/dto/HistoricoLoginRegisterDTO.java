package com.lodh8.starter.security.historico.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoricoLoginRegisterDTO {

    private String ip;

    private String userAgent;

    private String sessaoBffId;

    private String email;

    private String nome;

}
