package com.apg.clientes.dominio.repositorio;

import com.apg.clientes.dominio.modelo.Cliente;

import java.util.List;

public interface ClienteRepositorio {

    Cliente crear(Cliente cliente);
    Cliente actualizar(Cliente cliente);
    Cliente buscarPorId(Long id);
    List<Cliente> buscarTodos();
    List<Cliente> buscarPorPais(String codigoPais);
    boolean existeCorreo(String correo);
    boolean eliminarPorId(Long id);

}
