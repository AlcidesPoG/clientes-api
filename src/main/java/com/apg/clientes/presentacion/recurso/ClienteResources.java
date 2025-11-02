package com.apg.clientes.presentacion.recurso;

import com.apg.clientes.aplicacion.dto.ClienteParchearDto;
import com.apg.clientes.aplicacion.servicio.ClienteServicio;
import com.apg.clientes.aplicacion.dto.ClienteActualizarDto;
import com.apg.clientes.aplicacion.dto.ClienteCrearDto;
import com.apg.clientes.aplicacion.dto.ClienteRespuestaDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("/clientes")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Clientes", description = "Operaciones CRUD sobre clientes")
public class ClienteResources {

    private final ClienteServicio clienteServicio;

    @Inject
    public ClienteResources(ClienteServicio clienteServicio) {
        this.clienteServicio = clienteServicio;
    }

    @POST
    @Operation(summary = "Crear un nuevo cliente")
    public Response crear(@Valid @NotNull ClienteCrearDto request) {
        ClienteRespuestaDto clienteRespuesta = clienteServicio.crearCliente(request);
        return Response.status(Response.Status.CREATED).entity(clienteRespuesta).build();
    }

    @GET
    @Operation(summary = "Obtener todos los clientes")
    public Response obtenerTodos() {
        List<ClienteRespuestaDto> clientes = clienteServicio.buscarTodosLosClientes();
        return Response.ok(clientes).build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Obtener cliente por ID")
    public Response obtenerPorId(@PathParam("id") Long id) {
        ClienteRespuestaDto cliente = clienteServicio.buscarClientePorId(id);
        return Response.ok(cliente).build();
    }

    @GET
    @Path("/pais/{codigoPais}")
    @Operation(summary = "Obtener todos los clientes que pertecen a un pais")
    public Response obtenerPorPais(@PathParam("codigoPais")
                                       @Pattern(regexp = "^[A-Za-z]{3}$", message = "El codigo de pais debe estar en formato ISO 3166 Alpha-3. Ejemplos: (DOM o USA)")
                                       String codigoPais) {
        List<ClienteRespuestaDto> clientes = clienteServicio.buscarTodosLosClientesPorPais(codigoPais);
        return Response.ok(clientes).build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Actualiza completamente las partes a del cliente por ID")
    public Response actualizar(@PathParam("id") Long id, @Valid @NotNull ClienteActualizarDto clienteActualizarDTO) {
        ClienteRespuestaDto actualizado = clienteServicio.actualizarCliente(id, clienteActualizarDTO);
        return Response.ok(actualizado).build();
    }

    @PATCH
    @Path("/{id}")
    @Operation(summary = "Actualiza parcialmente las partes permitidas del cliente por ID")
    public Response parchear(@PathParam("id") Long id, @Valid @NotNull ClienteParchearDto clienteParchearDto) {
        ClienteRespuestaDto actualizado = clienteServicio.parchearCliente(id, clienteParchearDto);
        return Response.ok(actualizado).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Eliminar un cliente por ID")
    public Response eliminar(@PathParam("id") Long id) {
        clienteServicio.eliminarCliente(id);
        return Response.noContent().build();
    }


}
