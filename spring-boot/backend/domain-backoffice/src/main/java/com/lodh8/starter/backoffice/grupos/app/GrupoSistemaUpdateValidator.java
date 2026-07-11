package com.lodh8.starter.backoffice.grupos.app;

import com.herculanoleo.sentinelflow.exceptions.ValidatorException;
import com.herculanoleo.sentinelflow.validations.ValidationFactory;
import com.herculanoleo.sentinelflow.validator.Validate;
import com.herculanoleo.sentinelflow.validator.ValidatorFactory;
import com.lodh8.starter.backoffice.grupos.domain.GrupoSistemaUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GrupoSistemaUpdateValidator {

    private final ValidatorFactory validator;

    private final ValidationFactory validation;

    public void validate(GrupoSistemaUpdate update) throws ValidatorException {
        Validate logic = (validator, validations) -> validator.create(update)
                .field("nome", GrupoSistemaUpdate::nome)
                .add(validations.isNotBlank("nome é obrigatório"))
                .add(validations.lengthMin(2, "nome deve ter ao menos 2 caracteres"))
                .end()
                .validate();

        logic.validate(validator, validation);
    }

}
