package com.btg.operaciones.mappers;

import com.btg.operaciones.dto.ClienteDto;
import com.btg.operaciones.dto.FondoDto;
import com.btg.operaciones.dto.TransaccionDto;
import com.btg.operaciones.entities.Cliente;
import com.btg.operaciones.entities.Fondo;
import com.btg.operaciones.entities.Transaccion;
import com.btg.operaciones.enums.TipoNotificacionEnum;
import com.btg.operaciones.enums.TipoTransaccionEnum;
import org.springframework.stereotype.Component;

import javax.annotation.processing.Generated;
import java.util.List;

@Generated(
        value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class OperacionesMapperImpl  implements OperacionesMapper {


    @Override
    public Fondo fondoDtoToFondo(FondoDto fondoDto) {
        if(fondoDto == null){
            return null;
        }
        Fondo fondo = new Fondo();
        fondo.setNombre(fondoDto.getNombreFondo());
        fondo.setSaldoTotal(fondoDto.getSaldoFondo());
        fondo.setTipoFondo(fondoDto.getTipoFondo());
        fondo.setMontoMinimoApertura(fondoDto.getMontoMinimo());
        return fondo;
    }

    @Override
    public FondoDto fondoToFondoDto(Fondo fondo) {
        if(fondo == null){
            return null;
        }
        FondoDto fondoDto = new FondoDto();
        fondoDto.setIdFondo(fondo.getId());
        fondoDto.setNombreFondo(fondo.getNombre());
        fondoDto.setTipoFondo(fondo.getTipoFondo());
        fondoDto.setMontoMinimo(fondo.getMontoMinimoApertura());
        fondoDto.setSaldoFondo(fondo.getSaldoTotal());
        return fondoDto;
    }



    @Override
    public Cliente clienteDtoToCliente(ClienteDto clienteDto) {
        if(clienteDto == null){
            return null;
        }
        Cliente cliente = new Cliente();
        cliente.setNombre(clienteDto.getNombreCliente());
        cliente.setEmail(clienteDto.getCorreoElectronico());
        cliente.setTelefono(clienteDto.getNumeroCelular());
        cliente.setSaldo(clienteDto.getValorSaldo());
        return  cliente;

    }

    @Override
    public ClienteDto clienteToClienteDto(Cliente cliente) {
        if(cliente == null){
            return null;
        }
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setIdCliente(cliente.getId());
        clienteDto.setNombreCliente(cliente.getNombre());
        clienteDto.setCorreoElectronico(cliente.getEmail());
        clienteDto.setNumeroCelular(cliente.getTelefono());
        clienteDto.setValorSaldo(cliente.getSaldo());
        return clienteDto;
    }

    @Override
    public Transaccion transaccionDtoToTransaccion(TransaccionDto transaccionDto) {
        if(transaccionDto == null){
            return null;
        }
        Transaccion transaccion = new Transaccion();
        transaccion.setIdCliente(transaccionDto.getIdCliente());
        transaccion.setIdFondo(transaccionDto.getIdFondo());
        transaccion.setTipoTransaccion(transaccionDto.getTipoTransaccion().toString());
        transaccion.setTipoNotificacion(transaccionDto.getTipoNotificacion().toString());
        transaccion.setMonto(transaccionDto.getValorMonto());
        transaccion.setFecha(transaccionDto.getFechaTransaccion());
        return  transaccion;
    }

    @Override
    public TransaccionDto transaccionToTransaccionDto(Transaccion transaccion) {
        if(transaccion == null){
            return null;
        }
        TransaccionDto transaccionDto = new TransaccionDto();
        transaccionDto.setIdTransaccion(transaccion.getId());
        transaccionDto.setIdentificadorUnico(transaccion.getUniqueId().toString());
        transaccionDto.setIdCliente(transaccion.getIdCliente());
        transaccionDto.setIdFondo(transaccion.getIdFondo());
        transaccionDto.setTipoTransaccion(TipoTransaccionEnum.valueOf(transaccion.getTipoTransaccion()));
        transaccionDto.setTipoNotificacion(TipoNotificacionEnum.valueOf(transaccion.getTipoNotificacion()));
        transaccionDto.setValorMonto(transaccion.getMonto());
        transaccionDto.setFechaTransaccion(transaccion.getFecha());
        return  transaccionDto;
    }

    @Override
    public List<TransaccionDto> transaccionesToTransaccionesDto(List<Transaccion> transacciones) {

        if(transacciones == null){
            return null;
        }
        return transacciones.stream().map(this::transaccionToTransaccionDto).toList();
    }
}
