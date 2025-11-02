package com.apg.clientes.infraestructura.restcountries;

import com.apg.clientes.dominio.servicio.PaisServicio;
import com.apg.clientes.compartido.util.TextoUtil;
import com.apg.clientes.infraestructura.restcountries.dto.PaisApiDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import java.util.List;

@ApplicationScoped
public class PaisServicioImpl implements PaisServicio {

    private final PaisApi paisApi;

    private static final Logger LOG = Logger.getLogger(PaisServicioImpl.class);

    @Inject
    public PaisServicioImpl(@RestClient PaisApi paisApi) {
        this.paisApi = paisApi;
    }

    public String obtenerGentilicio(String codigoPais){
        if(TextoUtil.isNullOrBlank(codigoPais)) return null;

        try {
            List<PaisApiDTO> paises = paisApi.obtenerPaisPorCodigo(codigoPais);
            if (paises.isEmpty()) return null;

            PaisApiDTO pais = paises.getFirst();
            if (pais.getDemonyms() == null || pais.getDemonyms().getEng() == null) return  null;

            String masculino = pais.getDemonyms().getEng().getM();
            String femenino = pais.getDemonyms().getEng().getF();

            String gentilicio = "Masculino: " + masculino + " / Femenino: " + femenino;
            return gentilicio;

        }
        catch (Exception e) {
            //Se retorna nulo para poder continuar el proceso, debido a que entiendo que un error 404, debería ser que
            // el pais no existe en general y podría servir como validación, pero también podría significar que el
            // recurso se movió, además de otros errores y la dependencia que significaría lanzar una excepción aquí.
            LOG.warn("Error al obtener gentilicio: " + e.getMessage(), e);
            return null;
        }
    }
}
