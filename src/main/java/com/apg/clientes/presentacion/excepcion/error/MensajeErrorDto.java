package com.apg.clientes.presentacion.excepcion.error;

import java.time.LocalDateTime;
import java.util.List;

public class MensajeErrorDto {
    private String mensaje;
    private int codigoError;
    private LocalDateTime fecha;
    private List<String> detalles;


    public MensajeErrorDto() {}

    public MensajeErrorDto(String message, int codigoError) {
        this.mensaje = message;
        this.codigoError = codigoError;
        this.fecha = LocalDateTime.now();
    }

    public MensajeErrorDto(String message, int codigoError, List<String> detalles) {
        this.mensaje = message;
        this.codigoError = codigoError;
        this.fecha = LocalDateTime.now();
        this.detalles = detalles;
    }

    // Getters y setters
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public int getCodigoError() { return codigoError; }
    public void setCodigoError(int codigoError) { this.codigoError = codigoError; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public List<String> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<String> detalles) {
        this.detalles = detalles;
    }
}
