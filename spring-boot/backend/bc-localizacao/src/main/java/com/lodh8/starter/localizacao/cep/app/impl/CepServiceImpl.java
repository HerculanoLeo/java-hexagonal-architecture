package com.lodh8.starter.localizacao.cep.app.impl;

import com.lodh8.starter.localizacao.cep.app.CepService;
import com.lodh8.starter.localizacao.cep.app.port.CepProviderPort;
import com.lodh8.starter.localizacao.cep.domain.CEP;
import com.lodh8.starter.shared.utils.FormatTextUtils;
import com.lodh8.starter.shared.utils.IdentificadorLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CepServiceImpl implements CepService {

    private final List<CepProviderPort> ports;

    @Override
    public Optional<CEP> findByCep(String cep) {
        log.info("cep findByCep cep={}", IdentificadorLog.mascarar(cep));
        log.debug("cep findByCep cep={}", cep);
        var normalized = FormatTextUtils.onlyNumbers(cep);
        if (StringUtils.isNotBlank(normalized)) {
            for (var port : ports) {
                var result = port.findByCep(cep);
                if (result.isPresent()) {
                    log.info("cep findByCep cep={} resultado=ok", IdentificadorLog.mascarar(cep));
                    return result;
                }
            }
        }
        log.warn("cep findByCep cep={} resultado=nao_encontrado", IdentificadorLog.mascarar(cep));
        return Optional.empty();
    }


}