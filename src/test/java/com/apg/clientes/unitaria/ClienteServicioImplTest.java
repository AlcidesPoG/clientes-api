package com.apg.clientes.unitaria;


import com.apg.clientes.aplicacion.dto.ClienteActualizarDto;
import com.apg.clientes.aplicacion.dto.ClienteCrearDto;
import com.apg.clientes.aplicacion.dto.ClienteParchearDto;
import com.apg.clientes.aplicacion.dto.ClienteRespuestaDto;
import com.apg.clientes.dominio.excepcion.ClienteNoEncontradoException;
import com.apg.clientes.aplicacion.mapper.ClienteMapper;
import com.apg.clientes.aplicacion.mapper.ClienteMapperImpl;
import com.apg.clientes.aplicacion.servicio.implementacion.ClienteServicioImpl;
import com.apg.clientes.infraestructura.restcountries.PaisServicioImpl;
import com.apg.clientes.dominio.modelo.Cliente;
import com.apg.clientes.dominio.repositorio.ClienteRepositorio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ClienteServicioImplTest {
    @Mock
    ClienteRepositorio clienteRepositorio;

    @Mock
    PaisServicioImpl paisServicioImpl;

    @InjectMocks
    ClienteServicioImpl clienteServicio;

    private Cliente clienteBase;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ClienteMapper mapper = new ClienteMapperImpl();
        clienteServicio = new ClienteServicioImpl(clienteRepositorio, paisServicioImpl, mapper);

        clienteBase = new Cliente();
        clienteBase.setId(1L);
        clienteBase.setPrimerNombre("Juan");
        clienteBase.setSegundoNombre("Luis");
        clienteBase.setPrimerApellido("Perez");
        clienteBase.setSegundoApellido("Gomez");
        clienteBase.setCorreo("juan@test.com");
        clienteBase.setDireccion("Calle 1");
        clienteBase.setTelefono("8091234567");
        clienteBase.setPais("DOM");
        clienteBase.setGentilicio("Masculino: Dominican / Femenino: Dominican");

    }

    @Test
    void crearClienteDeberiaGuardarCorrectamente() {
        ClienteCrearDto dto = new ClienteCrearDto();
        dto.setPrimerNombre("Juan");
        dto.setSegundoNombre("Luis");
        dto.setPrimerApellido("Perez");
        dto.setSegundoApellido("Gomez");
        dto.setCorreo("juan@test.com");
        dto.setDireccion("Calle 1");
        dto.setTelefono("8091234567");
        dto.setPais("DOM");

        when(paisServicioImpl.obtenerGentilicio("DOM")).thenReturn("Masculino: Dominican / Femenino: Dominican");
        when(clienteRepositorio.crear(any(Cliente.class))).thenAnswer(invocation -> {
            Cliente cliente = invocation.getArgument(0);
            cliente.setId(1L);
            return cliente;
        });

        ClienteRespuestaDto resultado = clienteServicio.crearCliente(dto);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Juan", resultado.getPrimerNombre());
        assertEquals("Luis", resultado.getSegundoNombre());
        assertEquals("Perez", resultado.getPrimerApellido());
        assertEquals("Gomez", resultado.getSegundoApellido());
        assertEquals("juan@test.com", resultado.getCorreo());
        assertEquals("Calle 1", resultado.getDireccion());
        assertEquals("8091234567", resultado.getTelefono());
        assertEquals("DOM", resultado.getPais());
        assertEquals("Masculino: Dominican / Femenino: Dominican", resultado.getGentilicio());
        verify(clienteRepositorio, times(1)).crear(any(Cliente.class));
    }

    @Test
    void buscarClientePorIdDeberiaRetornarClienteExistente() {
        when(clienteRepositorio.buscarPorId(1L)).thenReturn(clienteBase);

        ClienteRespuestaDto resultado = clienteServicio.buscarClientePorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Juan", resultado.getPrimerNombre());
        assertEquals("Luis", resultado.getSegundoNombre());
        assertEquals("Perez", resultado.getPrimerApellido());
        assertEquals("Gomez", resultado.getSegundoApellido());
        assertEquals("juan@test.com", resultado.getCorreo());
        assertEquals("Calle 1", resultado.getDireccion());
        assertEquals("8091234567", resultado.getTelefono());
        assertEquals("DOM", resultado.getPais());
        assertEquals("Masculino: Dominican / Femenino: Dominican", resultado.getGentilicio());
        verify(clienteRepositorio).buscarPorId(1L);
    }

    @Test
    void buscarClientePorIdDeberiaLanzarExcepcionNoExiste() {
        when(clienteRepositorio.buscarPorId(9999L)).thenReturn(null);

        assertThrows(ClienteNoEncontradoException.class, () -> clienteServicio.buscarClientePorId(9999L));
        verify(clienteRepositorio).buscarPorId(9999L);
    }

    @Test
    void buscarTodosLosClientesDeberiaRetornarListaDeClientes() {
        when(clienteRepositorio.buscarTodos()).thenReturn(List.of(clienteBase));

        List<ClienteRespuestaDto> resultado = clienteServicio.buscarTodosLosClientes();

        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.getFirst().getId());
        assertEquals("Juan", resultado.getFirst().getPrimerNombre());
        assertEquals("Luis", resultado.getFirst().getSegundoNombre());
        assertEquals("Perez", resultado.getFirst().getPrimerApellido());
        assertEquals("Gomez", resultado.getFirst().getSegundoApellido());
        assertEquals("juan@test.com", resultado.getFirst().getCorreo());
        assertEquals("Calle 1", resultado.getFirst().getDireccion());
        assertEquals("8091234567", resultado.getFirst().getTelefono());
        assertEquals("DOM", resultado.getFirst().getPais());
        assertEquals("Masculino: Dominican / Femenino: Dominican", resultado.getFirst().getGentilicio());
        verify(clienteRepositorio).buscarTodos();
    }

    @Test
    void buscarTodosLosClientesPorPaisDeberiaFiltrarPorPais() {
        when(clienteRepositorio.buscarPorPais("DOM")).thenReturn(List.of(clienteBase));

        List<ClienteRespuestaDto> resultado = clienteServicio.buscarTodosLosClientesPorPais("DOM");

        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.getFirst().getId());
        assertEquals("Juan", resultado.getFirst().getPrimerNombre());
        assertEquals("Luis", resultado.getFirst().getSegundoNombre());
        assertEquals("Perez", resultado.getFirst().getPrimerApellido());
        assertEquals("Gomez", resultado.getFirst().getSegundoApellido());
        assertEquals("juan@test.com", resultado.getFirst().getCorreo());
        assertEquals("Calle 1", resultado.getFirst().getDireccion());
        assertEquals("8091234567", resultado.getFirst().getTelefono());
        assertEquals("DOM", resultado.getFirst().getPais());
        assertEquals("Masculino: Dominican / Femenino: Dominican", resultado.getFirst().getGentilicio());
        verify(clienteRepositorio).buscarPorPais("DOM");
    }

    @Test
    void actualizarClienteDeberiaActualizarYRetornarCliente() {
        ClienteActualizarDto clienteActualizado = new ClienteActualizarDto();
        clienteActualizado.setCorreo("juan.perez@ejemplo.com");
        clienteActualizado.setDireccion("Nueva calle");
        clienteActualizado.setTelefono("8090000000");
        clienteActualizado.setPais("MEX");

        when(clienteRepositorio.buscarPorId(1L)).thenReturn(clienteBase);
        when(paisServicioImpl.obtenerGentilicio("MEX")).thenReturn("Masculino: Mexican / Femenino: Mexican");
        when(clienteRepositorio.actualizar(any(Cliente.class))).thenReturn(clienteBase);

        ClienteRespuestaDto resultado = clienteServicio.actualizarCliente(1L, clienteActualizado);

        assertNotNull(resultado);
        assertEquals("juan.perez@ejemplo.com", resultado.getCorreo());
        assertEquals("Nueva calle", resultado.getDireccion());
        assertEquals("8090000000", resultado.getTelefono());
        assertEquals("MEX", resultado.getPais());
        assertEquals("Masculino: Mexican / Femenino: Mexican", resultado.getGentilicio());
        verify(clienteRepositorio).actualizar(any(Cliente.class));
    }

    @Test
    void actualizarClienteDeberiaLanzarExcepcionNoExiste() {
        when(clienteRepositorio.buscarPorId(9999L)).thenReturn(null);

        ClienteActualizarDto clienteActualizado = new ClienteActualizarDto();
        clienteActualizado.setCorreo("juan.perez@ejemplo.com");
        clienteActualizado.setDireccion("Nueva calle");
        clienteActualizado.setTelefono("8090000000");
        clienteActualizado.setPais("MEX");

        assertThrows(ClienteNoEncontradoException.class, () -> clienteServicio.actualizarCliente(9999L, clienteActualizado));
    }

    @Test
    void parchearClienteDeberiaActualizarParcialmente() {
        ClienteParchearDto parcheDto = new ClienteParchearDto();
        parcheDto.setCorreo("juan.perez@prueba.com");
        parcheDto.setPais("MEX");

        when(clienteRepositorio.buscarPorId(1L)).thenReturn(clienteBase);
        when(paisServicioImpl.obtenerGentilicio("MEX")).thenReturn("Masculino: Mexican / Femenino: Mexican");
        when(clienteRepositorio.actualizar(any(Cliente.class))).thenReturn(clienteBase);

        ClienteRespuestaDto resultado = clienteServicio.parchearCliente(1L, parcheDto);

        assertNotNull(resultado);
        assertEquals("juan.perez@prueba.com", resultado.getCorreo());
        assertEquals("MEX", resultado.getPais());
        assertEquals("Masculino: Mexican / Femenino: Mexican", resultado.getGentilicio());
        verify(clienteRepositorio).actualizar(any(Cliente.class));
    }

    @Test
    void parchearClienteDeberiaLanzarExcepcionNoExiste() {
        when(clienteRepositorio.buscarPorId(9999L)).thenReturn(null);

        ClienteParchearDto parcheDto = new ClienteParchearDto();
        parcheDto.setDireccion("Nueva calle");

        assertThrows(ClienteNoEncontradoException.class, () -> clienteServicio.parchearCliente(9999L, parcheDto));
        verify(clienteRepositorio).buscarPorId(9999L);
    }

    @Test
    void eliminarClienteDeberiaEliminarCorrectamente() {
        when(clienteRepositorio.eliminarPorId(1L)).thenReturn(true);

        boolean eliminado = clienteServicio.eliminarCliente(1L);

        assertTrue(eliminado);
        verify(clienteRepositorio).eliminarPorId(1L);
    }

    @Test
    void eliminarClienteDeberiaLanzarExcepcionNoExiste() {
        when(clienteRepositorio.eliminarPorId(9999L)).thenReturn(false);

        assertThrows(ClienteNoEncontradoException.class, () -> clienteServicio.eliminarCliente(9999L));
        verify(clienteRepositorio).eliminarPorId(9999L);
    }

}
