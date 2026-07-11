package com.lodh8.starter.location.cep.app.impl;

import com.lodh8.starter.location.cep.app.CepService;
import com.lodh8.starter.location.cep.app.port.CepProviderPort;
import com.lodh8.starter.location.cep.domain.CEP;
import com.lodh8.starter.shared.utils.FormatTextUtils;
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
