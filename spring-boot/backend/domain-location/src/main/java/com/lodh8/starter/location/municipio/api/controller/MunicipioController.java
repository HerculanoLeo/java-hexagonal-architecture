package com.lodh8.starter.location.municipio.api.controller;

import com.lodh8.starter.location.municipio.api.dto.MunicipioDTO;
import com.lodh8.starter.location.municipio.api.dto.MunicipioRegisterDTO;
import com.lodh8.starter.location.municipio.api.dto.MunicipioSearchDTO;
import com.lodh8.starter.location.municipio.api.dto.MunicipioUpdateDTO;
import com.lodh8.starter.location.municipio.app.MunicipioService;
import com.lodh8.starter.location.municipio.infra.MunicipioDTOMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/localizacao/municipio")
@RequiredArgsConstructor
public class MunicipioController {

    private final MunicipioService service;

    private final MunicipioDTOMapper mapper;

    @GetMapping
    public ResponseEntity<Collection<MunicipioDTO>> findAll(MunicipioSearchDTO searchDTO) {
        var municipios = service.findAll(mapper.domain(searchDTO))
                .stream().map(mapper::dto).toList();
        return ResponseEntity.ok(municipios);
    }

    @GetMapping("{id}")
    public ResponseEntity<MunicipioDTO> findById(@PathVariable Long id) {
        var dto = service.findById(id).map(mapper::dto);
        return ResponseEntity.of(dto);
    }

    @PostMapping
    public ResponseEntity<MunicipioDTO> register(@RequestBody MunicipioRegisterDTO request) {
        var domain = mapper.domain(request);
        var registered = service.register(domain);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.dto(registered));
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody MunicipioUpdateDTO request) {
        var domain = mapper.domain(request);
        service.update(id, domain);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
