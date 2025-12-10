package com.herculanoleo.starter.location.cep.app.impl;

import com.herculanoleo.starter.location.cep.app.CepService;
import com.herculanoleo.starter.location.cep.app.ports.CepProviderPort;
import com.herculanoleo.starter.location.cep.domain.CEP;
import com.herculanoleo.starter.shared.utils.FormatTextUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CepServiceImpl implements CepService {

    private final List<CepProviderPort> ports;

    @Override
    public Optional<CEP> findByCep(String cep) {
        var normalized = FormatTextUtils.onlyNumbers(cep);
        if (StringUtils.isNotBlank(normalized)) {
            for (var port : ports) {
                var result = port.findByCep(cep);
                if (result.isPresent()) {
                    return result;
                }
            }
        }
        return Optional.empty();
    }


}
