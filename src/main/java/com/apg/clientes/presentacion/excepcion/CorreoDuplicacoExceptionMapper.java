    package com.apg.clientes.presentacion.excepcion;

    import com.apg.clientes.dominio.excepcion.CorreoDuplicadoException;
    import com.apg.clientes.presentacion.excepcion.error.MensajeErrorDto;
    import jakarta.enterprise.context.ApplicationScoped;
    import org.jboss.resteasy.reactive.RestResponse;
    import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

    import java.util.List;

    @ApplicationScoped
    public class CorreoDuplicacoExceptionMapper {

        @ServerExceptionMapper
        public RestResponse<MensajeErrorDto> manejarCorreoDuplicado(CorreoDuplicadoException ex) {
            MensajeErrorDto error = new MensajeErrorDto(
                    ex.getMessage(),
                    400,
                    List.of("Ya existe un cliente con este correo electrónico.")
            );
            return RestResponse.status(RestResponse.Status.BAD_REQUEST, error);
        }
    }
