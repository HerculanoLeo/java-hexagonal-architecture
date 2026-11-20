package com.lodh8.starter.shared.utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class LogMdcTest {

    @AfterEach
    void tearDown() {
        LogMdc.clear();
    }

    @Test
    void putUidAndUsuarioThenClear() {
        LogMdc.putUid("uid-1");
        LogMdc.putUsuario("user-1");

        assertEquals("uid-1", LogMdc.uid());
        assertEquals("user-1", LogMdc.usuario());

        LogMdc.clear();

        assertNull(LogMdc.uid());
        assertNull(LogMdc.usuario());
    }

    @Test
    void blankRemovesKey() {
        LogMdc.putUid("uid-1");
        LogMdc.putUsuario("user-1");

        LogMdc.putUid(" ");
        LogMdc.putUsuario(null);

        assertNull(LogMdc.uid());
        assertNull(LogMdc.usuario());
    }
}
