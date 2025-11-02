package com.apg.clientes.aplicacion.mapper;

import com.apg.clientes.aplicacion.dto.ClienteParchearDto;
import com.apg.clientes.aplicacion.dto.ClienteCrearDto;
import com.apg.clientes.aplicacion.dto.ClienteRespuestaDto;
import com.apg.clientes.aplicacion.dto.ClienteActualizarDto;
import com.apg.clientes.dominio.modelo.Cliente;
import com.apg.clientes.compartido.util.TextoUtil;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "cdi",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        imports = TextoUtil.class
)
public interface ClienteMapper {

    //Establecer minusculas y mayusculas de ciertos campos
    @Mapping(target = "correo", qualifiedByName = "convertirMinusculas")
    @Mapping(target = "pais", qualifiedByName = "convertirMayusculas")
    Cliente convertirACliente(ClienteCrearDto dto);

    ClienteRespuestaDto convertirAClienteRespuesta(Cliente cliente);
    List<ClienteRespuestaDto> convertirAListaDeClienteRespuesta(List<Cliente> clientes);

    @Mapping(target = "correo", qualifiedByName = "convertirMinusculas")
    @Mapping(target = "pais", qualifiedByName = "convertirMayusculas")
    void actualizarDesdeDto(ClienteActualizarDto dto, @MappingTarget Cliente cliente);

    //Validaciones para no cambiar valores nulos al parchear (actualizar parcialmente)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "correo", qualifiedByName = "convertirMinusculas", conditionExpression = "java(!TextoUtil.isNullOrBlank(dto.getCorreo()))")
    @Mapping(target = "direccion", conditionExpression = "java(!TextoUtil.isNullOrBlank(dto.getDireccion()))")
    @Mapping(target = "telefono", conditionExpression = "java(!TextoUtil.isNullOrBlank(dto.getTelefono()))")
    @Mapping(target = "pais", qualifiedByName = "convertirMayusculas", conditionExpression = "java(!TextoUtil.isNullOrBlank(dto.getPais()))")
    void parchearDesdeDto(ClienteParchearDto dto, @MappingTarget Cliente cliente);

    @Named("convertirMayusculas")
    default String convertirMayusculas(String value) {
        return !TextoUtil.isNullOrBlank(value) ? value.toUpperCase() : null;
    }

    @Named("convertirMinusculas")
    default String convertirMinusculas(String value) {
        return !TextoUtil.isNullOrBlank(value) ? value.toLowerCase() : null;
    }

}
