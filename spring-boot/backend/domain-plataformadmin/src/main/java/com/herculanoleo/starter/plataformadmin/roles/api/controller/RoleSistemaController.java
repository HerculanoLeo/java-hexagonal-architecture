package com.herculanoleo.starter.plataformadmin.roles.api.controller;

import com.herculanoleo.starter.plataformadmin.roles.api.dtos.RoleSistemaDTO;
import com.herculanoleo.starter.plataformadmin.roles.app.RoleSistemaService;
import com.herculanoleo.starter.plataformadmin.roles.infra.RoleSistemaDTOMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/api/sistema/roles")
@RequiredArgsConstructor
public class RoleSistemaController {

    private final RoleSistemaService service;

    private final RoleSistemaDTOMapper mapper;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('admin-sistemas', 'grupos-sistemas')")
    public ResponseEntity<Collection<RoleSistemaDTO>> roles() {
        var roles = service.roles().stream().map(mapper::dto).toList();
        return ResponseEntity.ok(roles);
    }

}
