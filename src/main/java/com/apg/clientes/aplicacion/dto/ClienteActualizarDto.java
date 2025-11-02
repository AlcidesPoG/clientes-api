package com.apg.clientes.aplicacion.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ClienteActualizarDto {

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo no es valido")
    @Size(max=320, message = "El email no puede tener mas de 320 caracteres")
    private String correo;

    @NotBlank(message = "La direccion es obligatoria")
    @Size(max=500, message = "La direccion no puede tener mas de 500 caracteres")
    private String direccion;

    @NotBlank(message = "El telefono es obligatorio")
    @Pattern(regexp = "^\\+?\\d{0,3}\\s?\\d{10}$", message = "El telefono tiene un formato incorrecto. Usa uno de estos formatos: 8091111111, +1801111111, +1 8091111111")
    @Size(max=20, message = "El telefono no puede tener mas de 20 caracteres")
    private String telefono;

    @NotBlank(message = "El pais es obligatorio")
    @Pattern(regexp = "^[A-Za-z]{3}$", message = "El codigo de pais debe estar en formato ISO 3166 Alpha-3. Ejemplos: (DOM o USA)")
    @Size(min = 3, max=3, message = "El codigo de pais debe de estar en el formato ISO 3166 por lo que solo puede tener 3 caracteres")
    private String pais;

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }
}
