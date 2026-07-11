package com.lodh8.starter.security.sessao.api.controller;

import com.lodh8.starter.security.sessao.app.SessaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("seguranca/sessoes")
@RequiredArgsConstructor
public class SessaoController {

    private final SessaoService service;

    @DeleteMapping("{identityId}")
    @PreAuthorize("hasAnyAuthority('admin-sistemas', 'seguranca-usuarios-ativos-sistemas')")
    public ResponseEntity<Void> invalidateByIdentityId(
            @PathVariable("identityId") String identityId
    ) {
        this.service.invalidateByIdentityId(identityId);
        return ResponseEntity.accepted().build();
    }

}
