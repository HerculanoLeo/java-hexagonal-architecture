package com.herculanoleo.starter.shared.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

@UtilityClass
public class FormatTextUtils {

    public static String onlyNumbers(String value) {
        return StringUtils.defaultString(value).replaceAll("\\D", "");
    }

}
