package com.apg.clientes.presentacion.excepcion;

import com.apg.clientes.presentacion.excepcion.error.MensajeErrorDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.BadRequestException;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ValidacionExceptionMapper {

    private static final Logger LOG = Logger.getLogger(ValidacionExceptionMapper.class);

    // Se encarga de recibir los errores de validacion y cuáles validaciones fallaron
    @ServerExceptionMapper
    public RestResponse<MensajeErrorDto> manejarValidaciones(ConstraintViolationException ex) {
        List<String> detalles = ex.getConstraintViolations()
                .stream()
                .map(this::formatoDeViolacion)
                .collect(Collectors.toList());

        MensajeErrorDto error = new MensajeErrorDto("Errores de validacion", 400, detalles);
        return RestResponse.status(RestResponse.Status.BAD_REQUEST, error);
    }

    @ServerExceptionMapper
    public RestResponse<MensajeErrorDto> manejarBadRequest(BadRequestException ex) {
        MensajeErrorDto error = new MensajeErrorDto("La solicitud es invalida", 400, List.of("Verifique la sintaxis del JSON", "Verifique que el json este bien formado"));
        return RestResponse.status(RestResponse.Status.BAD_REQUEST, error);
    }

    private String formatoDeViolacion(ConstraintViolation<?> v) {
        return v.getPropertyPath() + ": " + v.getMessage();
    }
}
