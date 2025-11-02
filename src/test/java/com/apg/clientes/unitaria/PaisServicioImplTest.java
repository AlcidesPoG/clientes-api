package com.apg.clientes.unitaria;

import com.apg.clientes.infraestructura.restcountries.PaisServicioImpl;
import com.apg.clientes.infraestructura.restcountries.PaisApi;
import com.apg.clientes.infraestructura.restcountries.dto.PaisApiDTO;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PaisServicioImplTest {
    @Mock
    PaisApi paisApi;

    PaisServicioImpl paisServicioImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        paisServicioImpl = new PaisServicioImpl(paisApi);
    }

    private List<PaisApiDTO> crearPaisRestDto(String m, String f) {
        PaisApiDTO.Language lenguaje = new PaisApiDTO.Language(f, m);
        PaisApiDTO.Demonyms gentilicio = new PaisApiDTO.Demonyms(lenguaje);
        PaisApiDTO pais = new PaisApiDTO(gentilicio);
        return List.of(pais);
    }

    @Test
    void deberiaRetornarGentilicioCorrectoCuandoApiRespondeValido() throws Exception {
        List<PaisApiDTO> paises = crearPaisRestDto("Dominican", "Dominican");
        when(paisApi.obtenerPaisPorCodigo("DOM")).thenReturn(paises  );

        String resultado = paisServicioImpl.obtenerGentilicio("DOM");

        assertNotNull(resultado);
        assertTrue(resultado.contains("Masculino: Dominican"));
        assertTrue(resultado.contains("Femenino: Dominican"));
        verify(paisApi).obtenerPaisPorCodigo("DOM");
    }


    @Test
    void deberiaRetornarNullCuandoSeEnviaCodigoInvalido() throws Exception {
        when(paisApi.obtenerPaisPorCodigo("AAA")).thenThrow(new NotFoundException());

        String resultado = paisServicioImpl.obtenerGentilicio("AAA");

        assertNull(resultado);
        verify(paisApi).obtenerPaisPorCodigo("AAA");
    }
}
