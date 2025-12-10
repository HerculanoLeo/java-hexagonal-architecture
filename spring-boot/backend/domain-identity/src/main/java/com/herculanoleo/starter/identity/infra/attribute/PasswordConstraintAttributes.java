package com.herculanoleo.starter.identity.infra.attribute;

import lombok.Data;
import org.passay.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "api.security.password")
@Data
public class PasswordConstraintAttributes {

    private Integer upper = 1;

    private Integer digit = 1;

    private Integer lower = 1;

    private Integer special = 1;

    public List<Rule> generateRules() {
        return Arrays.asList(new LengthRule(4, 30), new CharacterRule(EnglishCharacterData.UpperCase, upper),
                new CharacterRule(EnglishCharacterData.LowerCase, lower),
                new CharacterRule(EnglishCharacterData.Digit, digit),
                new CharacterRule(EnglishCharacterData.Special, special), new WhitespaceRule());
    }

}
