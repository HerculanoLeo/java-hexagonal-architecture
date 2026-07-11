package com.lodh8.starter.notification.api.controller;

import com.lodh8.starter.notification.app.NotificacaoTesteService;
import com.lodh8.starter.notification.domain.NotificacaoTesteTipo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("notificacao/teste")
@RequiredArgsConstructor
public class NotificacaoTesteController {

    private final NotificacaoTesteService service;

    @PostMapping("email/{tipo}")
    @PreAuthorize("hasAuthority('admin-sistemas')")
    public ResponseEntity<Void> testarEmail(@PathVariable NotificacaoTesteTipo tipo) {
        service.enviarEmailTeste(tipo);
        return ResponseEntity.accepted().build();
    }

}
