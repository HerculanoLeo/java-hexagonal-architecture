package com.lodh8.starter.backoffice.usuarios.app;

import com.herculanoleo.sentinelflow.exceptions.ValidatorException;
import com.herculanoleo.sentinelflow.validations.ValidationFactory;
import com.herculanoleo.sentinelflow.validator.Validate;
import com.herculanoleo.sentinelflow.validator.ValidatorFactory;
import com.lodh8.starter.backoffice.usuarios.domain.UsuarioSistemaUpdate;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsuarioSistemaUpdateValidator {

    private final ValidatorFactory validator;

    private final ValidationFactory validation;

    public void validate(UsuarioSistemaUpdate update) throws ValidatorException {
        Validate logic = (validator, validations) -> validator.create(update)
                .field("nome", UsuarioSistemaUpdate::nome)
                .add(validations.isNotBlank("nome é obrigatório"))
                .add(validations.lengthMin(2, "nome deve ter ao menos 2 caracteres"))
                .end()
                .field("grupoId", UsuarioSistemaUpdate::grupoId)
                .add((value) -> {
                    if (!update.main() && StringUtils.isBlank(value)) {
                        return validation.invalid("grupo é obrigatório");
                    }
                    return validation.valid();
                })
                .end()
                .validate();

        logic.validate(validator, validation);
    }

}
