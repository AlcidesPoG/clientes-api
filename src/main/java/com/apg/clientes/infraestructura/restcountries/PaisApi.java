package com.apg.clientes.infraestructura.restcountries;

import com.apg.clientes.infraestructura.restcountries.dto.PaisApiDTO;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@Path("/v3.1/alpha")
@RegisterRestClient(configKey = "restcountries-api")
public interface PaisApi {

    @GET
    @Path("/{codigo}")
    @Produces(MediaType.APPLICATION_JSON)
    List<PaisApiDTO> obtenerPaisPorCodigo(@PathParam("codigo") String codigopais);

}
