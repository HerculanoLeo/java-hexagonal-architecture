package com.lodh8.starter.backoffice.usuarios.app;

import com.herculanoleo.sentinelflow.exceptions.ValidatorException;
import com.herculanoleo.sentinelflow.validations.ValidationFactory;
import com.herculanoleo.sentinelflow.validator.Validate;
import com.herculanoleo.sentinelflow.validator.ValidatorFactory;
import com.lodh8.starter.backoffice.usuarios.domain.UsuarioSistemaRegister;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsuarioSistemaRegisterValidator {

    private final ValidatorFactory validator;

    private final ValidationFactory validation;

    public void validate(UsuarioSistemaRegister register) throws ValidatorException {
        Validate logic = (validator, validations) -> validator.create(register)
                .field("nome", UsuarioSistemaRegister::nome)
                .add(validations.isNotBlank("nome é obrigatório"))
                .add(validations.lengthMin(2, "nome deve ter ao menos 2 caracteres"))
                .end()
                .field("email", UsuarioSistemaRegister::email)
                .add(validations.isNotBlank("e-mail é obrigatório"))
                .add(validations.email("e-mail inválido"))
                .end()
                .field("grupoId", UsuarioSistemaRegister::grupoId)
                .add((value) -> {
                    if (!register.main() && StringUtils.isBlank(value)) {
                        return validation.invalid("grupo é obrigatório");
                    }
                    return validation.valid();
                })
                .end()
                .validate();

        logic.validate(validator, validation);
    }

}
