package com.herculanoleo.starter.plataformadmin.grupos.api.controller;

import com.herculanoleo.starter.plataformadmin.grupos.api.dtos.GrupoSistemaDTO;
import com.herculanoleo.starter.plataformadmin.grupos.api.dtos.GrupoSistemaRegisterRequest;
import com.herculanoleo.starter.plataformadmin.grupos.api.dtos.GrupoSistemaSearchRequest;
import com.herculanoleo.starter.plataformadmin.grupos.api.dtos.GrupoSistemaUpdateRequest;
import com.herculanoleo.starter.plataformadmin.grupos.app.GrupoSistemaService;
import com.herculanoleo.starter.plataformadmin.grupos.infra.GrupoSistemaDTOMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/sistema/grupo")
@RequiredArgsConstructor
public class GrupoSistemaController {

    private final GrupoSistemaService service;

    private final GrupoSistemaDTOMapper mapper;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('admin-sistemas', 'usuarios-sistemas', 'grupos-sistemas')")
    public ResponseEntity<Collection<GrupoSistemaDTO>> findAll(GrupoSistemaSearchRequest requestEntity) {
        var grupos = this.service.findAll(mapper.search(requestEntity))
                .stream().map(mapper::dto).toList();
        return ResponseEntity.ok(grupos);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyAuthority('admin-sistemas', 'usuarios-sistemas', 'grupos-sistemas')")
    public ResponseEntity<GrupoSistemaDTO> findById(@PathVariable("id") String id) {
        var grupo = this.service.findById(id).map(mapper::dto);
        return ResponseEntity.of(grupo);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('admin-sistemas', 'grupos-sistemas')")
    public ResponseEntity<GrupoSistemaDTO> register(@RequestBody GrupoSistemaRegisterRequest requestEntity) {
        var grupo = this.service.register(mapper.register(requestEntity));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.dto(grupo));
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAnyAuthority('admin-sistemas', 'grupos-sistemas')")
    public ResponseEntity<Void> update(@PathVariable("id") String id, GrupoSistemaUpdateRequest requestEntity) {
        this.service.update(id, mapper.update(requestEntity));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyAuthority('admin-sistemas', 'grupos-sistemas')")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        this.service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
