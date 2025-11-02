package com.apg.clientes.unitaria;

import com.apg.clientes.aplicacion.dto.ClienteActualizarDto;
import com.apg.clientes.aplicacion.dto.ClienteCrearDto;
import com.apg.clientes.aplicacion.dto.ClienteParchearDto;
import com.apg.clientes.aplicacion.dto.ClienteRespuestaDto;
import com.apg.clientes.aplicacion.servicio.implementacion.ClienteServicioImpl;
import com.apg.clientes.presentacion.recurso.ClienteResources;
import jakarta.persistence.NoResultException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ClienteResourceImplTest {

    @Mock
    ClienteServicioImpl clienteServicio;

    @InjectMocks
    ClienteResources clienteResources;

    private ClienteCrearDto crearDto;
    private ClienteActualizarDto actualizarDto;
    private ClienteParchearDto parchearDto;
    private ClienteRespuestaDto respuestaDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        crearDto = new ClienteCrearDto();
        crearDto.setPrimerNombre("Juan");
        crearDto.setSegundoNombre("Luis");
        crearDto.setPrimerApellido("Perez");
        crearDto.setSegundoApellido("Gomez");
        crearDto.setCorreo("juan@test.com");
        crearDto.setDireccion("Calle 1");
        crearDto.setTelefono("8091234567");
        crearDto.setPais("DOM");

        actualizarDto = new ClienteActualizarDto();
        actualizarDto.setCorreo("juan.perez@ejemplo.com");
        actualizarDto.setDireccion("Nueva calle");
        actualizarDto.setTelefono("8090000000");
        actualizarDto.setPais("MEX");

        parchearDto = new ClienteParchearDto();
        parchearDto.setCorreo("juan.perez@nuevo.com");
        parchearDto.setDireccion("Nueva calle 2");

        respuestaDto = new ClienteRespuestaDto();
        respuestaDto.setId(1L);
        respuestaDto.setPrimerNombre("Juan");
        respuestaDto.setSegundoNombre("Luis");
        respuestaDto.setPrimerApellido("Perez");
        respuestaDto.setSegundoApellido("Gomez");
        respuestaDto.setCorreo("juan@test.com");
        respuestaDto.setDireccion("Calle 1");
        respuestaDto.setTelefono("8091234567");
        respuestaDto.setPais("DOM");
        respuestaDto.setGentilicio("Masculino: Dominican / Femenino: Dominican");
    }

    @Test
    void crearClienteDeberiaRetornarCreated() {
        when(clienteServicio.crearCliente(any(ClienteCrearDto.class))).thenReturn(respuestaDto);

        Response respuesta = clienteResources.crear(crearDto);

        assertEquals(Response.Status.CREATED.getStatusCode(), respuesta.getStatus());
        ClienteRespuestaDto body = (ClienteRespuestaDto) respuesta.getEntity();
        assertEquals(1L, body.getId());
        assertEquals("Juan", body.getPrimerNombre());
        assertEquals("Luis", body.getSegundoNombre());
        assertEquals("Perez", body.getPrimerApellido());
        assertEquals("Gomez", body.getSegundoApellido());
        assertEquals("juan@test.com", body.getCorreo());
        assertEquals("Calle 1", body.getDireccion());
        assertEquals("8091234567", body.getTelefono());
        assertEquals("DOM", body.getPais());
        assertEquals("Masculino: Dominican / Femenino: Dominican", body.getGentilicio());
        verify(clienteServicio).crearCliente(any(ClienteCrearDto.class));
    }

    @Test
    void crearClienteErrorLanzaExcepcion() {
        when(clienteServicio.crearCliente(any())).thenThrow(new RuntimeException("Error interno"));

        assertThrows(RuntimeException.class, () -> clienteResources.crear(crearDto));
        verify(clienteServicio).crearCliente(any());
    }

    @Test
    void buscarTodosDeberiaRetornarListaClientes() {
        when(clienteServicio.buscarTodosLosClientes()).thenReturn(List.of(respuestaDto));

        Response respuesta = clienteResources.obtenerTodos();

        assertEquals(Response.Status.OK.getStatusCode(), respuesta.getStatus());
        List<ClienteRespuestaDto> lista = (List<ClienteRespuestaDto>) respuesta.getEntity();
        assertEquals(1, lista.size());
        verify(clienteServicio).buscarTodosLosClientes();
    }

    @Test
    void buscarPorIdDeberiaRetornarCliente() {
        when(clienteServicio.buscarClientePorId(1L)).thenReturn(respuestaDto);

        Response respuesta = clienteResources.obtenerPorId(1L);

        assertEquals(Response.Status.OK.getStatusCode(), respuesta.getStatus());
        ClienteRespuestaDto body = (ClienteRespuestaDto) respuesta.getEntity();
        assertEquals(1L, body.getId());
        assertEquals("Juan", body.getPrimerNombre());
        assertEquals("Luis", body.getSegundoNombre());
        assertEquals("Perez", body.getPrimerApellido());
        assertEquals("Gomez", body.getSegundoApellido());
        assertEquals("juan@test.com", body.getCorreo());
        assertEquals("Calle 1", body.getDireccion());
        assertEquals("8091234567", body.getTelefono());
        assertEquals("DOM", body.getPais());
        assertEquals("Masculino: Dominican / Femenino: Dominican", body.getGentilicio());
        verify(clienteServicio).buscarClientePorId(1L);
    }

    @Test
    void buscarPorPaisDeberiaRetornarListaClientes() {
        when(clienteServicio.buscarTodosLosClientesPorPais("DOM"))
                .thenReturn(List.of(respuestaDto));

        Response respuesta = clienteResources.obtenerPorPais("DOM");

        assertEquals(Response.Status.OK.getStatusCode(), respuesta.getStatus());
        List<ClienteRespuestaDto> lista = (List<ClienteRespuestaDto>) respuesta.getEntity();
        assertEquals(1, lista.size());
        assertEquals(1L, lista.getFirst().getId());
        assertEquals("Juan", lista.getFirst().getPrimerNombre());
        assertEquals("Luis",lista.getFirst().getSegundoNombre());
        assertEquals("Perez", lista.getFirst().getPrimerApellido());
        assertEquals("Gomez", lista.getFirst().getSegundoApellido());
        assertEquals("juan@test.com", lista.getFirst().getCorreo());
        assertEquals("Calle 1", lista.getFirst().getDireccion());
        assertEquals("8091234567", lista.getFirst().getTelefono());
        assertEquals("DOM", lista.getFirst().getPais());
        assertEquals("Masculino: Dominican / Femenino: Dominican", lista.getFirst().getGentilicio());
        verify(clienteServicio).buscarTodosLosClientesPorPais("DOM");
    }

    @Test
    void buscarPorPaisErrorLanzaExcepcion() {
        when(clienteServicio.buscarTodosLosClientesPorPais("AAA"))
                .thenThrow(new RuntimeException("Error en el servicio"));

        assertThrows(RuntimeException.class, () -> clienteResources.obtenerPorPais("AAA"));
        verify(clienteServicio).buscarTodosLosClientesPorPais("AAA");
    }

    @Test
    void actualizarClienteDeberiaRetornar200YClienteRespuetaActualizado() {
        ClienteRespuestaDto clienteActualizado = new ClienteRespuestaDto();
        clienteActualizado.setId(1L);
        clienteActualizado.setPrimerNombre("Juan");
        clienteActualizado.setPrimerApellido("Perez");
        clienteActualizado.setCorreo("juan.perez@ejemplo.com");
        clienteActualizado.setDireccion("Nueva calle");
        clienteActualizado.setTelefono("8090000000");
        clienteActualizado.setPais("MEX");
        clienteActualizado.setGentilicio("Masculino: Mexican / Femenino: Mexican");

        when(clienteServicio.actualizarCliente(1L, actualizarDto)).thenReturn(clienteActualizado);

        Response respuesta = clienteResources.actualizar(1L, actualizarDto);

        assertEquals(Response.Status.OK.getStatusCode(), respuesta.getStatus());
        ClienteRespuestaDto body = (ClienteRespuestaDto) respuesta.getEntity();
        assertEquals("juan.perez@ejemplo.com", body.getCorreo());
        assertEquals("Nueva calle", body.getDireccion());
        assertEquals("8090000000", body.getTelefono());
        assertEquals("MEX", body.getPais());
        assertEquals("Masculino: Mexican / Femenino: Mexican", body.getGentilicio());
        verify(clienteServicio).actualizarCliente(1L, actualizarDto);
    }

    @Test
    void actualizarClienteNoEncontradoDeberiaLanzarNoResultException() {
        when(clienteServicio.actualizarCliente(9999L, actualizarDto))
                .thenThrow(new NoResultException("Cliente no encontrado"));

        assertThrows(NoResultException.class, () -> clienteResources.actualizar(9999L, actualizarDto));
        verify(clienteServicio).actualizarCliente(9999L, actualizarDto);
    }

    @Test
    void parchearClienteDeberiaRetornar200YClienteRespuestaParcheado() {
        ClienteRespuestaDto clienteParcheado = new ClienteRespuestaDto();
        clienteParcheado.setId(1L);
        clienteParcheado.setPrimerNombre("Juan");
        clienteParcheado.setPrimerApellido("Perez");
        clienteParcheado.setCorreo("juan.perez@nuevo.com");
        clienteParcheado.setDireccion("Nueva calle 2");
        clienteParcheado.setTelefono("8091234567");
        clienteParcheado.setPais("DOM");
        clienteParcheado.setGentilicio("Masculino: Dominican / Femenino: Dominican");

        when(clienteServicio.parchearCliente(1L, parchearDto)).thenReturn(clienteParcheado);

        Response respuesta = clienteResources.parchear(1L, parchearDto);

        assertEquals(Response.Status.OK.getStatusCode(), respuesta.getStatus());
        ClienteRespuestaDto body = (ClienteRespuestaDto) respuesta.getEntity();
        assertEquals("Juan", body.getPrimerNombre());
        assertEquals("Perez", body.getPrimerApellido());
        assertEquals("juan.perez@nuevo.com", body.getCorreo());
        assertEquals("Nueva calle 2", body.getDireccion());
        assertEquals("8091234567", body.getTelefono());
        assertEquals("DOM", body.getPais());
        assertEquals("Masculino: Dominican / Femenino: Dominican", body.getGentilicio());
        verify(clienteServicio).parchearCliente(1L, parchearDto);
    }

    @Test
    void parchearClienteNoEncontradoDeberiaLanzarNoResultException() {
        when(clienteServicio.parchearCliente(9999L, parchearDto))
                .thenThrow(new NoResultException("Cliente no encontrado"));

        assertThrows(NoResultException.class, () -> clienteResources.parchear(9999L, parchearDto));
        verify(clienteServicio).parchearCliente(9999L, parchearDto);
    }


    @Test
    void eliminarClienteDeberiaRetornarNoContent() {
        when(clienteServicio.eliminarCliente(1L)).thenReturn(true); // 👈 retorna boolean

        Response respuesta = clienteResources.eliminar(1L);

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), respuesta.getStatus());
        assertNull(respuesta.getEntity());
        verify(clienteServicio).eliminarCliente(1L);
    }

    @Test
    void eliminarClienteNoEncontradoDeberiaLanzarNoResultException() {
        when(clienteServicio.eliminarCliente(9999L))
                .thenThrow(new jakarta.persistence.NoResultException("Cliente no encontrado"));

        assertThrows(jakarta.persistence.NoResultException.class, () -> clienteResources.eliminar(9999L));
        verify(clienteServicio).eliminarCliente(9999L);
    }
}
