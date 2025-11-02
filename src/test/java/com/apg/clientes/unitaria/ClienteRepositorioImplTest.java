package com.apg.clientes.unitaria;

import com.apg.clientes.dominio.modelo.Cliente;
import com.apg.clientes.infraestructura.persistencia.ClienteRepositorioImpl;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ClienteRepositorioImplTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private jakarta.persistence.TypedQuery<Cliente> query;

    @Mock
    private jakarta.persistence.TypedQuery<Long> countQuery;

    @InjectMocks
    private ClienteRepositorioImpl clienteRepositorioImpl;

    private Cliente clienteBase;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

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
    void crearDeberiaPersistirCliente() {
        Cliente resultado = clienteRepositorioImpl.crear(clienteBase);

        verify(entityManager, times(1)).persist(clienteBase);
        assertEquals(clienteBase, resultado);
    }

    @Test
    void actualizarDeberiaHacerMergeYRetornarCliente() {
        when(entityManager.merge(any(Cliente.class))).thenReturn(clienteBase);

        Cliente resultado = clienteRepositorioImpl.actualizar(clienteBase);

        verify(entityManager, times(1)).merge(clienteBase);
        assertEquals(clienteBase, resultado);
    }

    @Test
    void buscarPorIdDeberiaRetornarCliente() {
        when(entityManager.find(Cliente.class, 1L)).thenReturn(clienteBase);

        Cliente resultado = clienteRepositorioImpl.buscarPorId(1L);

        verify(entityManager).find(Cliente.class, 1L);
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
    }

    @Test
    void buscarPorIdDeberiaDevolverNullNoExiste() {
        when(entityManager.find(Cliente.class, 9999L)).thenReturn(null);

        Cliente resultado = clienteRepositorioImpl.buscarPorId(9999L);

        verify(entityManager).find(Cliente.class, 9999L);
        assertNull(resultado);
    }

    @Test
    void buscarTodosDeberiaEjecutarQueryYRetornarLista() {
        String consultaQuery = "FROM Cliente c ORDER BY c.id";
        when(entityManager.createQuery(consultaQuery, Cliente.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(clienteBase));

        List<Cliente> resultado = clienteRepositorioImpl.buscarTodos();

        verify(entityManager).createQuery(consultaQuery, Cliente.class);
        verify(query).getResultList();
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
    }

    @Test
    void buscarPorPaisDeberiaRetornarListaFiltradaPorPais() {
        String consultaQuery = "FROM Cliente c WHERE UPPER(c.pais) = :pais ORDER BY c.id";
        when(entityManager.createQuery(consultaQuery, Cliente.class)).thenReturn(query);
        when(query.setParameter(eq("pais"), anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(clienteBase));

        List<Cliente> resultado = clienteRepositorioImpl.buscarPorPais("DOM");

        verify(entityManager).createQuery(consultaQuery, Cliente.class);
        verify(query).setParameter("pais", "DOM");
        verify(query).getResultList();
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
    }

    @Test
    void eliminarPorIdDeberiaEliminarClienteExiste() {
        when(entityManager.find(Cliente.class, 1L)).thenReturn(clienteBase);

        boolean eliminado = clienteRepositorioImpl.eliminarPorId(1L);

        verify(entityManager).remove(clienteBase);
        assertTrue(eliminado);
    }

    @Test
    void eliminarPorIdDeberiaRetornarFalsoNoExiste() {
        when(entityManager.find(Cliente.class, 9999L)).thenReturn(null);

        boolean eliminado = clienteRepositorioImpl.eliminarPorId(9999L);

        verify(entityManager, never()).remove(any());
        assertFalse(eliminado);
    }

    @Test
    void existeCorreoDeberiaRetornarTrueExiste() {
        String consultaQuery = "SELECT COUNT(c) FROM Cliente c WHERE LOWER(c.correo) = :correo";
        String correoBuscado = "juan@test.com";
        Long conteoExistente = 1L;

        when(entityManager.createQuery(consultaQuery, Long.class))
                .thenReturn(countQuery);
        when(countQuery.setParameter(eq("correo"), eq(correoBuscado.toLowerCase())))
                .thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(conteoExistente);

        boolean existe = clienteRepositorioImpl.existeCorreo(correoBuscado);

        verify(entityManager).createQuery(consultaQuery, Long.class);
        verify(countQuery).setParameter("correo", correoBuscado.toLowerCase());
        verify(countQuery).getSingleResult();
        assertTrue(existe, "Debería retornar true si el conteo es 1");
    }

    @Test
    void existeCorreoDeberiaRetornarFalseSiNoExiste() {
        String consultaQuery = "SELECT COUNT(c) FROM Cliente c WHERE LOWER(c.correo) = :correo";
        String correoNoExistente = "noexiste@test.com";
        Long conteoNoExistente = 0L;

        when(entityManager.createQuery(consultaQuery, Long.class))
                .thenReturn(countQuery);
        when(countQuery.setParameter(eq("correo"), anyString()))
                .thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(conteoNoExistente);

        boolean existe = clienteRepositorioImpl.existeCorreo(correoNoExistente);

        verify(entityManager).createQuery(consultaQuery, Long.class);
        verify(countQuery).setParameter("correo", correoNoExistente.toLowerCase());
        verify(countQuery).getSingleResult();
        assertFalse(existe, "Debería retornar false si el conteo es 0");
    }

}
