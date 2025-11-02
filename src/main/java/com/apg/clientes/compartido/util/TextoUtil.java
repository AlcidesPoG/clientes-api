package com.apg.clientes.compartido.util;

public final class TextoUtil {

    private TextoUtil() {

    }

    public static boolean isNullOrBlank(String texto){
        return texto == null || texto.isBlank();
    }
}
