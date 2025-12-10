package com.herculanoleo.starter.identity.usuario.infra.passy;

import com.herculanoleo.starter.identity.infra.attribute.PasswordConstraintAttributes;
import com.herculanoleo.starter.identity.usuario.app.port.PasswordConstraintProviderPort;
import com.herculanoleo.starter.shared.exceptions.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.passay.CharacterRule;
import org.passay.PasswordData;
import org.passay.PasswordGenerator;
import org.passay.PasswordValidator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class PasswordConstraintPassyAdapter implements PasswordConstraintProviderPort {

    private final PasswordValidator passwordValidator;

    private final PasswordConstraintAttributes attributes;

    @Override
    public void validate(String password) {
        var result = passwordValidator.validate(new PasswordData(password));

        if (!result.isValid()) {
            throw new BadRequestException(
                    String.format("Senha não corresponde aos requisitos mínimos de segurança." +
                            " A senha deve conter letras maiúsculas, minúsculas, números e" +
                            " pelo menos %s caractere(s) especial", this.attributes.getSpecial())
            );
        }
    }

    @Override
    public String generateRandomPassword() {
        var rules = new ArrayList<CharacterRule>();

        for (var rule : this.attributes.generateRules()) {
            if (rule instanceof CharacterRule characterRule) {
                rules.add(characterRule);
            }
        }
        var generator = new PasswordGenerator();
        return generator.generatePassword(8, rules);
    }
}
