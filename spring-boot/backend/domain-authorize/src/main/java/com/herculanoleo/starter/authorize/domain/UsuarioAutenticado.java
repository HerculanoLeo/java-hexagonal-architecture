package com.herculanoleo.starter.authorize.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.herculanoleo.starter.shared.models.enums.Status;
import com.herculanoleo.starter.shared.models.enums.TipoAcesso;

import java.util.Objects;
import java.util.UUID;

public record UsuarioAutenticado(
        UUID id,
        String identityId,
        String relacionadoId,
        String nome,
        String email,
        TipoAcesso tipo,
        Status status,
        Integer versao
) {

    public UsuarioAutenticado(TipoAcesso tipo) {
        this(null, null, null, null, null, tipo, Status.ATIVO, null);
    }

    @JsonIgnore
    public boolean isSistema() {
        return TipoAcesso.USUARIO_SISTEMA.equals(this.tipo)
                || TipoAcesso.CLIENTE_SISTEMA.equals(this.tipo);
    }

    @JsonIgnore
    public boolean equalsRelacionadoId(UUID relacionadoId) {
        return equalsRelacionadoId(relacionadoId.toString());
    }

    @JsonIgnore
    public boolean equalsRelacionadoId(String relacionadoId) {
        return Objects.equals(relacionadoId, this.relacionadoId);
    }

}
