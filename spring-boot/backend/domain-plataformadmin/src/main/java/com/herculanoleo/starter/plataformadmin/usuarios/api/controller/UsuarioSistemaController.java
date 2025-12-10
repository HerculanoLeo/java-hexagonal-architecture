package com.herculanoleo.starter.plataformadmin.usuarios.api.controller;

import com.herculanoleo.starter.plataformadmin.usuarios.api.dtos.*;
import com.herculanoleo.starter.plataformadmin.usuarios.app.UsuarioSistemaService;
import com.herculanoleo.starter.plataformadmin.usuarios.infra.UsuarioSistemaDTOMapper;
import com.herculanoleo.starter.shared.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("/api/sistema/usuario")
@RequiredArgsConstructor
public class UsuarioSistemaController {

    private final UsuarioSistemaService service;

    private final UsuarioSistemaDTOMapper mapper;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('admin-sistemas', 'usuarios-sistemas')")
    public ResponseEntity<Collection<UsuarioSistemaDTO>> findAll(UsuarioSistemaSearchRequest requestEntity) {
        var usuarios = service.findAll(mapper.domain(requestEntity));
        return ResponseEntity.ok(usuarios.stream().map(mapper::dto).toList());
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyAuthority('admin-sistemas', 'usuarios-sistemas')")
    public ResponseEntity<UsuarioSistemaDTO> findById(@PathVariable("id") UUID id) {
        var usuario = service.findById(id).orElseThrow(() -> new NotFoundException("usuário não encontrado"));
        return ResponseEntity.ok(mapper.dto(usuario));
    }

    @GetMapping("{id}/grupo")
    @PreAuthorize("hasAnyAuthority('admin-sistemas', 'usuarios-sistemas')")
    public ResponseEntity<UsuarioSistemaGrupoDTO> findGrupoById(@PathVariable("id") UUID id) {
        var usuario = service.findGrupoById(id).orElseThrow(() -> new NotFoundException("usuário ou grupo não encontrado"));
        return ResponseEntity.ok(mapper.dto(usuario));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('admin-sistemas', 'usuarios-sistemas')")
    public ResponseEntity<UsuarioSistemaDTO> register(@RequestBody UsuarioSistemaRegisterRequest requestEntity) {
        var usuario = service.register(mapper.domain(requestEntity));
        return ResponseEntity.ok(mapper.dto(usuario));
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAnyAuthority('admin-sistemas', 'usuarios-sistemas')")
    public ResponseEntity<Void> update(@PathVariable("id") UUID id, @RequestBody UsuarioSistemaUpdateRequest requestEntity) {
        service.update(id, mapper.domain(requestEntity));
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}/ativar")
    @PreAuthorize("hasAnyAuthority('admin-sistemas', 'usuarios-sistemas')")
    public ResponseEntity<Void> ativar(@PathVariable("id") UUID id) {
        service.ativar(id);
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping("{id}/inativar")
    @PreAuthorize("hasAnyAuthority('admin-sistemas', 'usuarios-sistemas')")
    public ResponseEntity<Void> inativar(@PathVariable("id") UUID id) {
        service.inativar(id);
        return ResponseEntity.accepted().build();
    }

    @PutMapping("{id}/reset-password")
    @PreAuthorize("hasAnyAuthority('admin-sistemas', 'usuarios-sistemas')")
    public ResponseEntity<Void> resetPassword(@PathVariable("id") UUID id) {
        service.resetPassword(id);
        return ResponseEntity.accepted().build();
    }

}
