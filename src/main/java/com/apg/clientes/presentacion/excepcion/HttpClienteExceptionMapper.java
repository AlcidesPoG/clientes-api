package com.apg.clientes.presentacion.excepcion;

import com.apg.clientes.presentacion.excepcion.error.MensajeErrorDto;

import com.apg.clientes.presentacion.excepcion.error.MensajesErrorHttp;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.WebApplicationException;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import java.util.List;

@ApplicationScoped
public class HttpClienteExceptionMapper {

    private static final Logger LOG = Logger.getLogger(HttpClienteExceptionMapper.class);

    // Tuve un error en un JSON que un bad request no atrapaba, porque salía el error desde WebApplicationException
    // por eso cree este manejador de excepciones, vi que la documentación también recomendaba usar esta implementación.
    @ServerExceptionMapper
    public RestResponse<MensajeErrorDto> manejarErroresHttpCliente(WebApplicationException ex) {
        int codigo = (ex.getResponse() != null)
                ? ex.getResponse().getStatus()
                : RestResponse.Status.INTERNAL_SERVER_ERROR.getStatusCode();

        String mensaje = MensajesErrorHttp.obtenerMensajePorCodigo(codigo);
        String detalle = MensajesErrorHttp.obtenerDetallesPorCodigo(codigo);

        if (codigo >= 500)
            LOG.errorf("Error HTTP %d: %s (%s)", codigo, mensaje, ex.getMessage());

        MensajeErrorDto error = new MensajeErrorDto(mensaje, codigo, List.of(detalle)
        );
        RestResponse.Status status = RestResponse.Status.fromStatusCode(codigo);
        return RestResponse.status(status, error);
    }
}
