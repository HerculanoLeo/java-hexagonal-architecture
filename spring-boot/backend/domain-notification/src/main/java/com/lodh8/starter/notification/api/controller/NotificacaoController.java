package com.lodh8.starter.notification.api.controller;

import com.lodh8.starter.notification.api.dto.NotificacaoDTO;
import com.lodh8.starter.notification.api.dto.NotificacaoSearchDTO;
import com.lodh8.starter.notification.app.NotificacaoService;
import com.lodh8.starter.notification.app.NotificacaoTesteService;
import com.lodh8.starter.notification.infra.NotificacaoDTOMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("notificacao")
@RequiredArgsConstructor
public class NotificacaoController {

    private final NotificacaoService service;

    private final NotificacaoTesteService testeService;

    private final NotificacaoDTOMapper mapper;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('admin-sistemas', 'notificacao-sistemas')")
    public ResponseEntity<Collection<NotificacaoDTO>> findAll(NotificacaoSearchDTO requestEntity) {
        var notificacoes = this.service.findAll(this.mapper.domain(requestEntity));
        return ResponseEntity.ok(notificacoes.stream().map(this.mapper::dto).toList());
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyAuthority('admin-sistemas', 'notificacao-sistemas')")
    public ResponseEntity<NotificacaoDTO> findById(@PathVariable UUID id) {
        var notificacao = this.service.findById(id).map(this.mapper::dto);
        return ResponseEntity.of(notificacao);
    }

    @PostMapping("email/teste")
    @PreAuthorize("hasAuthority('admin-sistemas')")
    public ResponseEntity<Void> testeEmail() {
        this.testeService.enviarEmailTeste();
        return ResponseEntity.accepted().build();
    }

}
