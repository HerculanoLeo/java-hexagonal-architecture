package com.lodh8.starter.authorize.api.controller;

import com.lodh8.starter.authorize.api.dto.GrupoAutenticadoDTO;
import com.lodh8.starter.authorize.api.dto.TrocaSenhaRequest;
import com.lodh8.starter.authorize.api.dto.UsuarioAutenticadoDTO;
import com.lodh8.starter.authorize.api.dto.UsuarioAutenticadoUpdateRequest;
import com.lodh8.starter.authorize.app.UsuarioAutenticadoService;
import com.lodh8.starter.authorize.infra.UsuarioAutenticadoDTOMapper;
import com.lodh8.starter.identity.usuario.domain.TrocaSenha;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UsuarioAutenticadoController {

    private final UsuarioAutenticadoService service;

    private final UsuarioAutenticadoDTOMapper mapper;

    @GetMapping("me")
    public ResponseEntity<UsuarioAutenticadoDTO> me() {
        var domain = service.usuarioAutenticado();
        return ResponseEntity.ok(mapper.dto(domain));
    }

    @PutMapping("me")
    public ResponseEntity<UsuarioAutenticadoDTO> updateMe(@Valid @RequestBody UsuarioAutenticadoUpdateRequest request) {
        var domain = service.updateMe(request.nome());
        return ResponseEntity.ok(mapper.dto(domain));
    }

    @PutMapping("me/senha")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody TrocaSenhaRequest request) {
        service.changePassword(new TrocaSenha(
                request.senhaAtual(),
                request.novaSenha(),
                request.confirmacaoSenha()
        ));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("grupo")
    public ResponseEntity<GrupoAutenticadoDTO> grupo() {
        var domain = service.grupoAutenticado();
        if (domain == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(mapper.dto(domain));
    }

}
