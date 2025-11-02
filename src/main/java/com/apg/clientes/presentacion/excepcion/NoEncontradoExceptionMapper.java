package com.apg.clientes.presentacion.excepcion;

import com.apg.clientes.dominio.excepcion.ClienteNoEncontradoException;
import com.apg.clientes.presentacion.excepcion.error.MensajeErrorDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

@ApplicationScoped
public class NoEncontradoExceptionMapper {

    @ServerExceptionMapper
    public RestResponse<MensajeErrorDto> manejarClienteNoEncontrado(ClienteNoEncontradoException ex) {
        MensajeErrorDto error = new MensajeErrorDto("El cliente no fue encontrado", 404);
        return RestResponse.status(RestResponse.Status.NOT_FOUND, error);
    }

    @ServerExceptionMapper
    public RestResponse<MensajeErrorDto> manejarRecursoNoEncontrado(NotFoundException ex) {
        MensajeErrorDto error = new MensajeErrorDto("El recurso no existe, verifique la url", 404);
        return RestResponse.status(RestResponse.Status.NOT_FOUND, error);
    }
}
