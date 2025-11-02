package com.apg.clientes.aplicacion.servicio.implementacion;

import com.apg.clientes.aplicacion.dto.ClienteActualizarDto;
import com.apg.clientes.aplicacion.dto.ClienteCrearDto;
import com.apg.clientes.aplicacion.dto.ClienteParchearDto;
import com.apg.clientes.aplicacion.dto.ClienteRespuestaDto;
import com.apg.clientes.aplicacion.mapper.ClienteMapper;
import com.apg.clientes.aplicacion.servicio.ClienteServicio;
import com.apg.clientes.compartido.util.TextoUtil;
import com.apg.clientes.dominio.excepcion.ClienteNoEncontradoException;
import com.apg.clientes.dominio.excepcion.CorreoDuplicadoException;
import com.apg.clientes.dominio.modelo.Cliente;
import com.apg.clientes.dominio.repositorio.ClienteRepositorio;
import com.apg.clientes.dominio.servicio.PaisServicio;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class ClienteServicioImpl implements ClienteServicio {

    private final ClienteRepositorio clienteRepositorio;
    private final PaisServicio paisServicio;
    private final ClienteMapper clienteMapper;

    @Inject
    public ClienteServicioImpl(ClienteRepositorio clienteRepositorio, PaisServicio paisServicio,
                               ClienteMapper clienteMapper) {
        this.clienteRepositorio = clienteRepositorio;
        this.paisServicio = paisServicio;
        this.clienteMapper = clienteMapper;
    }

    public ClienteRespuestaDto crearCliente(ClienteCrearDto clienteCrear) {

        validarCorreoExiste(clienteCrear.getCorreo(), null);

        Cliente cliente = clienteMapper.convertirACliente(clienteCrear);

        asignarGentilicio(cliente);

        clienteRepositorio.crear(cliente);
        return clienteMapper.convertirAClienteRespuesta(cliente);
    }

    public ClienteRespuestaDto buscarClientePorId(long id) {
        Cliente cliente = obtenerClientePorId(id);
        return clienteMapper.convertirAClienteRespuesta(cliente);
    }

    public List<ClienteRespuestaDto> buscarTodosLosClientes() {
        List<Cliente> clientes = clienteRepositorio.buscarTodos();
        return clienteMapper.convertirAListaDeClienteRespuesta(clientes);
    }

    public List<ClienteRespuestaDto> buscarTodosLosClientesPorPais(String pais) {
        List<Cliente> clientes = clienteRepositorio.buscarPorPais(pais);
        return clienteMapper.convertirAListaDeClienteRespuesta(clientes);
    }

    public ClienteRespuestaDto actualizarCliente(long id, ClienteActualizarDto clienteActualizado) {

        Cliente cliente = obtenerClientePorId(id);

        validarCorreoExiste(clienteActualizado.getCorreo(), cliente.getCorreo());

        clienteMapper.actualizarDesdeDto(clienteActualizado, cliente);

        asignarGentilicio(cliente);

        clienteRepositorio.actualizar(cliente);
        return clienteMapper.convertirAClienteRespuesta(cliente);
    }

    public ClienteRespuestaDto parchearCliente(long id, ClienteParchearDto clienteParcheado) {

        Cliente cliente = obtenerClientePorId(id);

        validarCorreoExiste(clienteParcheado.getCorreo(), cliente.getCorreo());

        clienteMapper.parchearDesdeDto(clienteParcheado, cliente);

        asignarGentilicio(cliente);

        clienteRepositorio.actualizar(cliente);
        return clienteMapper.convertirAClienteRespuesta(cliente);
    }

    public boolean eliminarCliente(long id) {
        boolean eliminado = clienteRepositorio.eliminarPorId(id);
        if (!eliminado)
            throw new ClienteNoEncontradoException("Cliente no encontrado");
        return true;
    }

    private Cliente obtenerClientePorId(long id) {
        Cliente cliente = clienteRepositorio.buscarPorId(id);
        if (cliente == null) {
            throw new ClienteNoEncontradoException("Cliente no encontrado");
        }
        return cliente;
    }

    private void validarCorreoExiste(String correoNuevo, String correoActual) {
        if(TextoUtil.isNullOrBlank(correoNuevo)) return;

        String correoNuevoMinusculas = correoNuevo.trim().toLowerCase();
        String correoActualMinusculas = correoActual != null ? correoActual.trim().toLowerCase() : null;

        if (!correoNuevoMinusculas.equals(correoActualMinusculas) &&
                clienteRepositorio.existeCorreo(correoNuevoMinusculas))
        {
            throw new CorreoDuplicadoException("Este correo ya ha sido registrado");
        }
    }

    private void asignarGentilicio(Cliente cliente) {
        String pais = cliente.getPais();

        if (TextoUtil.isNullOrBlank(pais)) {
            return;
        }

        String gentilicio = paisServicio.obtenerGentilicio(pais);
        if (!TextoUtil.isNullOrBlank(gentilicio)) {
            cliente.setGentilicio(gentilicio);
        }
    }

}
