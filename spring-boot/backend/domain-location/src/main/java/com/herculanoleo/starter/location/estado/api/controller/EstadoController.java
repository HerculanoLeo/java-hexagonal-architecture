package com.herculanoleo.starter.location.estado.api.controller;

import com.herculanoleo.starter.location.estado.api.dto.EstadoDTO;
import com.herculanoleo.starter.location.estado.api.dto.EstadoSearchDTO;
import com.herculanoleo.starter.location.estado.app.EstadoService;
import com.herculanoleo.starter.location.estado.infra.EstadoDTOMapper;
import com.herculanoleo.starter.shared.models.enums.EstadoSigla;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/api/localizacao/estado")
@RequiredArgsConstructor
public class EstadoController {

    private final EstadoService service;

    private final EstadoDTOMapper mapper;

    @GetMapping
    public ResponseEntity<Collection<EstadoDTO>> findAll(EstadoSearchDTO requestEntity) {
        var entities = service.findAll(mapper.domain(requestEntity));
        return ResponseEntity.ok(entities.stream().map(mapper::dto).toList());
    }

    @GetMapping("{id}")
    public ResponseEntity<EstadoDTO> findById(@PathVariable("id") Long id) {
        var dto = service.findById(id).map(mapper::dto);
        return ResponseEntity.of(dto);
    }

    @GetMapping("sigla/{sigla}")
    public ResponseEntity<EstadoDTO> findBySigla(@PathVariable("sigla") EstadoSigla sigla) {
        var dto = service.findBySigla(sigla).map(mapper::dto);
        return ResponseEntity.of(dto);
    }

}
