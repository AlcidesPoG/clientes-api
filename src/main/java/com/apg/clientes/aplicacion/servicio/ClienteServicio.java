package com.apg.clientes.aplicacion.servicio;

import com.apg.clientes.aplicacion.dto.ClienteActualizarDto;
import com.apg.clientes.aplicacion.dto.ClienteCrearDto;
import com.apg.clientes.aplicacion.dto.ClienteParchearDto;
import com.apg.clientes.aplicacion.dto.ClienteRespuestaDto;

import java.util.List;

public interface ClienteServicio
{
    ClienteRespuestaDto crearCliente(ClienteCrearDto clienteCrear);
    ClienteRespuestaDto buscarClientePorId(long id);
    List<ClienteRespuestaDto> buscarTodosLosClientes();
    List<ClienteRespuestaDto> buscarTodosLosClientesPorPais(String pais);
    ClienteRespuestaDto actualizarCliente(long id, ClienteActualizarDto clienteActualizado);
    ClienteRespuestaDto parchearCliente(long id, ClienteParchearDto clienteParcheado);
    boolean eliminarCliente(long id) ;
}
