package com.apg.clientes.presentacion.excepcion;

import com.apg.clientes.presentacion.excepcion.error.MensajeErrorDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.PersistenceException;
import org.hibernate.exception.JDBCConnectionException;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import java.sql.SQLTransientConnectionException;
import java.util.List;

@ApplicationScoped
public class ConexionExceptionMapper {

    private static final Logger LOG = Logger.getLogger(ConexionExceptionMapper.class);

    @ServerExceptionMapper
    public RestResponse<MensajeErrorDto> manejarErrorDeConexionBD(PersistenceException ex) {
        Throwable causa = buscarCausaDelError(ex);

        // Se encarga de enviar un error porque el servicio no se pudo conectar con la base de datos.
        // Entiendo que el error revela mucho más de lo que debería. Lo retorno asi por ser una prueba.
        if (causa instanceof JDBCConnectionException ||
                causa instanceof SQLTransientConnectionException ||
                (causa != null && causa.getMessage() != null && causa.getMessage().contains("Connection"))) {

            LOG.error("Fallo al conectarse a la base de datos: " + causa.getMessage(), ex);

            MensajeErrorDto error = new MensajeErrorDto(
                    "Error al conectarse a la base de datos",
                    RestResponse.Status.SERVICE_UNAVAILABLE.getStatusCode(),
                    List.of("Verifique la configuracion y la disponibilidad de la base de datos")
            );

            return RestResponse.status(RestResponse.Status.SERVICE_UNAVAILABLE, error);
        }
        LOG.error("Error interno de la base de datos", ex);

        MensajeErrorDto error = new MensajeErrorDto("Error interno del servidor", 500);
        return RestResponse.status(RestResponse.Status.INTERNAL_SERVER_ERROR, error);
    }

    private Throwable buscarCausaDelError(Throwable e) {
        Throwable result = e;
        while (result.getCause() != null && result.getCause() != result) {
            result = result.getCause();
        }
        return result;
    }
}
