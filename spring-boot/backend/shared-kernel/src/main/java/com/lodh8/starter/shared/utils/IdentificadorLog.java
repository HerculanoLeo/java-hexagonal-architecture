package com.lodh8.starter.shared.utils;

/**
 * Mascara identificadores em logs INFO (prefixo + sufixo; miolo em asterisco).
 * Usar no par INFO mascarado / DEBUG aberto.
 */
public final class IdentificadorLog {

    private IdentificadorLog() {
    }

    public static String mascarar(Object valor) {
        if (valor == null) {
            return "-";
        }
        return mascarar(valor.toString());
    }

    public static String mascarar(String valor) {
        if (valor == null || valor.isBlank()) {
            return "-";
        }
        var texto = valor.trim();
        if (texto.length() <= 4) {
            return "*".repeat(texto.length());
        }
        var inicio = Math.min(3, texto.length() - 3);
        var fim = 2;
        return texto.substring(0, inicio)
                + "*".repeat(texto.length() - inicio - fim)
                + texto.substring(texto.length() - fim);
    }
}
