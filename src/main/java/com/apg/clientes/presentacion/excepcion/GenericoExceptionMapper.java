package com.apg.clientes.presentacion.excepcion;

import com.apg.clientes.presentacion.excepcion.error.MensajeErrorDto;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

@ApplicationScoped
public class GenericoExceptionMapper {

    private static final Logger LOG = Logger.getLogger(GenericoExceptionMapper.class);

    //Si no ocurre ninguno de los errores especificados anteriormente se enviara este error
    @ServerExceptionMapper
    public RestResponse<MensajeErrorDto> manejarGenerico(Throwable ex) {

        LOG.error("Error interno no controlado", ex);

        MensajeErrorDto error = new MensajeErrorDto("Error interno del servidor", 500);
        return RestResponse.status(RestResponse.Status.INTERNAL_SERVER_ERROR, error);
    }
}