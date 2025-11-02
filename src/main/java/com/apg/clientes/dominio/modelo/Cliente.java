package com.apg.clientes.dominio.modelo;

import jakarta.persistence.*;


@Entity
@Table(name = "cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="primer_nombre", nullable=false, length=50)
    private String primerNombre;

    @Column(name = "segundo_nombre", length=50)
    private String segundoNombre;

    @Column(name = "primer_apellido", nullable = false, length=50)
    private String primerApellido;

    @Column(name="segundo_apellido", length=50)
    private String segundoApellido;

    @Column(name = "correo", nullable = false, unique = true, length=320)
    private String correo;

    @Column(name = "direccion", nullable = false, length=500)
    private String direccion;

    @Column(name = "telefono", nullable = false, length=20)
    private String telefono;

    @Column(name = "pais", nullable = false, length = 3)
    private String pais;

    @Column(name = "gentilicio", length = 255)
    private String gentilicio;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrimerNombre() {
        return primerNombre;
    }

    public void setPrimerNombre(String primerNombre) {
        this.primerNombre = primerNombre;
    }

    public String getSegundoNombre() {
        return segundoNombre;
    }

    public void setSegundoNombre(String segundoNombre) {
        this.segundoNombre = segundoNombre;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

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

    public String getGentilicio() {
        return gentilicio;
    }

    public void setGentilicio(String gentilicio) {
        this.gentilicio = gentilicio;
    }


}
