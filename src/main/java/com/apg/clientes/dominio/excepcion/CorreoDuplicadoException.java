package com.apg.clientes.dominio.excepcion;

import jakarta.ws.rs.BadRequestException;

public class CorreoDuplicadoException extends BadRequestException {
    public CorreoDuplicadoException(String mensaje) {
        super(mensaje);
    }
}