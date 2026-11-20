package com.lodh8.starter.shared.utils;

import org.slf4j.MDC;

/**
 * Contexto MDC do processo de log: {@code uid} (correlacionador) e {@code usuario} (opcional).
 */
public final class LogMdc {

    public static final String UID = "uid";
    public static final String USUARIO = "usuario";

    private LogMdc() {
    }

    public static void putUid(String uid) {
        if (uid == null || uid.isBlank()) {
            MDC.remove(UID);
            return;
        }
        MDC.put(UID, uid);
    }

    public static void putUsuario(String usuario) {
        if (usuario == null || usuario.isBlank()) {
            MDC.remove(USUARIO);
            return;
        }
        MDC.put(USUARIO, usuario);
    }

    public static String uid() {
        return MDC.get(UID);
    }

    public static String usuario() {
        return MDC.get(USUARIO);
    }

    public static void clear() {
        MDC.remove(UID);
        MDC.remove(USUARIO);
    }
}
