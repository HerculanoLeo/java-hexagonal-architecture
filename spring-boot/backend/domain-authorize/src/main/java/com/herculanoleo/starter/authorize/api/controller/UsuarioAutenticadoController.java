package com.herculanoleo.starter.authorize.api.controller;

import com.herculanoleo.starter.authorize.api.dto.GrupoAutenticadoDTO;
import com.herculanoleo.starter.authorize.api.dto.UsuarioAutenticadoDTO;
import com.herculanoleo.starter.authorize.app.UsuarioAutenticadoService;
import com.herculanoleo.starter.authorize.infra.UsuarioAutenticadoDTOMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UsuarioAutenticadoController {

    private final UsuarioAutenticadoService service;

    private final UsuarioAutenticadoDTOMapper mapper;

    @GetMapping("me")
    public ResponseEntity<UsuarioAutenticadoDTO> me() {
        var domain = service.usuarioAutenticado();
        return ResponseEntity.ok(mapper.dto(domain));
    }

    @GetMapping("grupo")
    public ResponseEntity<GrupoAutenticadoDTO> grupo() {
        var domain = service.grupoAutenticado();
        return ResponseEntity.ok(mapper.dto(domain));
    }

}
