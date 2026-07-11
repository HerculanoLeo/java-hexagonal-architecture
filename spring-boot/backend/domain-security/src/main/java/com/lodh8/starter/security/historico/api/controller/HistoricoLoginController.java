package com.lodh8.starter.security.historico.api.controller;

import com.lodh8.starter.security.historico.api.dto.HistoricoLoginDTO;
import com.lodh8.starter.security.historico.api.dto.HistoricoLoginRegisterDTO;
import com.lodh8.starter.security.historico.api.dto.HistoricoLoginSearchDTO;
import com.lodh8.starter.security.historico.app.HistoricoLoginService;
import com.lodh8.starter.security.historico.infra.HistoricoLoginDTOMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("seguranca/historico-logins")
@RequiredArgsConstructor
public class HistoricoLoginController {

    private final HistoricoLoginService service;

    private final HistoricoLoginDTOMapper mapper;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('admin-sistemas', 'seguranca-historico-login-sistemas')")
    public ResponseEntity<Collection<HistoricoLoginDTO>> findAll(HistoricoLoginSearchDTO requestEntity) {
        var historicos = this.service.findAll(this.mapper.domain(requestEntity));
        return ResponseEntity.ok(historicos.stream().map(this.mapper::dto).toList());
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> register(@RequestBody(required = false) HistoricoLoginRegisterDTO requestEntity) {
        var body = requestEntity != null ? requestEntity : new HistoricoLoginRegisterDTO();
        this.service.register(
                body.getIp(),
                body.getUserAgent(),
                body.getSessaoBffId(),
                body.getEmail(),
                body.getNome()
        );
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

}
