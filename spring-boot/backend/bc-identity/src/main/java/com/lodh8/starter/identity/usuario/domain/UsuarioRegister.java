package com.lodh8.starter.identity.usuario.domain;

import com.lodh8.starter.shared.models.enums.TipoAcesso;
import com.lodh8.starter.shared.utils.AttributesMapper;

public record UsuarioRegister(
        boolean enabled,
        boolean main,
        boolean emailVerified,
        String relacionadoId,
        TipoAcesso tipo,
        String nome,
        String email,
        String senha,
        String grupoId,
        RedirectAction redirectAction
) implements AttributesMapper {
    public UsuarioRegister(
            boolean enabled,
            boolean main,
            boolean emailVerified,
            String nome,
            String email,
            TipoAcesso tipo,
            String gruposId,
            RedirectAction redirectAction
    ) {
        this(enabled, main, emailVerified, null, tipo, nome, email, null, gruposId, redirectAction);
    }
}
