package com.apg.clientes.dominio.excepcion;

import jakarta.ws.rs.NotFoundException;

public class ClienteNoEncontradoException extends NotFoundException {
  public ClienteNoEncontradoException(String mensaje) {
    super(mensaje);
  }
}
