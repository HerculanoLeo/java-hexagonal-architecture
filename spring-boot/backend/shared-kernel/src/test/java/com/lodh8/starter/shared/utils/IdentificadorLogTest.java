package com.lodh8.starter.shared.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class IdentificadorLogTest {

    @Test
    void mascaraIdentificadores() {
        assertEquals("ABC**23", IdentificadorLog.mascarar("ABC1D23"));
        assertEquals("9BW************51", IdentificadorLog.mascarar("9BWZZZ377VT004251"));
        assertEquals("adm*******************mx", IdentificadorLog.mascarar("administrador@dummy.comx"));
        assertEquals("-", IdentificadorLog.mascarar("  "));
        assertEquals("****", IdentificadorLog.mascarar("abcd"));
        assertEquals("-", IdentificadorLog.mascarar((Object) null));
        assertFalse(IdentificadorLog.mascarar("ABC1D23").contains("1D"));
    }
}
