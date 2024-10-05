package com.btg.operaciones.mappers;

import com.btg.operaciones.dto.ClienteDto;
import com.btg.operaciones.dto.FondoDto;
import com.btg.operaciones.dto.TransaccionDto;
import com.btg.operaciones.entities.Cliente;
import com.btg.operaciones.entities.Fondo;
import com.btg.operaciones.entities.Transaccion;
import org.mapstruct.Mapper;

import java.util.List;


@Mapper(componentModel = "spring")
public interface OperacionesMapper {

    Fondo fondoDtoToFondo(FondoDto fondoDto);

    FondoDto fondoToFondoDto(Fondo fondo) ;

    Cliente clienteDtoToCliente(ClienteDto clienteDto);

    ClienteDto clienteToClienteDto(Cliente cliente);

    Transaccion transaccionDtoToTransaccion(TransaccionDto transaccionDto);

    TransaccionDto transaccionToTransaccionDto(Transaccion transaccion);

    List<TransaccionDto> transaccionesToTransaccionesDto(List<Transaccion> transacciones);



}
