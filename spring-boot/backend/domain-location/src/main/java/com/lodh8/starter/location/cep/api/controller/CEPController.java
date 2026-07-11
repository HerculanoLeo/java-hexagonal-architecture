package com.lodh8.starter.location.cep.api.controller;

import com.lodh8.starter.location.cep.api.dto.CEPDTO;
import com.lodh8.starter.location.cep.app.CepService;
import com.lodh8.starter.location.cep.infra.CEPDTOMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("localizacao/cep")
@RequiredArgsConstructor
public class CEPController {

    private final CepService service;

    private final CEPDTOMapper mapper;

    @GetMapping("{cep}")
    public ResponseEntity<CEPDTO> findByCep(@PathVariable("cep") String cep) {
        var dto = service.findByCep(cep).map(mapper::dto);
        return ResponseEntity.of(dto);
    }

}
