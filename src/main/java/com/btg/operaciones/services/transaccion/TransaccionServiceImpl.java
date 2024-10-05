package com.btg.operaciones.services.transaccion;

import com.btg.operaciones.dto.ClienteDto;
import com.btg.operaciones.dto.FondoDto;
import com.btg.operaciones.entities.Transaccion;
import com.btg.operaciones.dto.NotificacionDto;
import com.btg.operaciones.dto.TransaccionDto;
import com.btg.operaciones.enums.OpcionesOrdenamientoTransEnum;
import com.btg.operaciones.enums.TipoTransaccionEnum;
import com.btg.operaciones.handlers.CustomExceptions.RequestInvalidoException;
import com.btg.operaciones.handlers.CustomExceptions.ObjetoNoEncontradoException;
import com.btg.operaciones.handlers.CustomExceptions.ServidorException;
import com.btg.operaciones.mappers.OperacionesMapper;
import com.btg.operaciones.repository.TransaccionRepository;
import com.btg.operaciones.services.notificador.NotificadorService;
import com.btg.operaciones.services.cliente.ClienteServiceImpl;
import com.btg.operaciones.services.fondo.FondoServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransaccionServiceImpl implements TransaccionService {


    @Value("${constantes.mensaje.cliente.noEcontrado}")
    private String msgClienteNoEncontrado;

    @Value("${constantes.mensaje.fondo.noEcontrado}")
    private String msgFondoNoEncontrado;

    @Value("${constantes.mensaje.error.guardar.transaccion}")
    private String msgErrorGuardarTransaccion;

    @Value("${constantes.mensaje.monto.minimo.vinculacion}")
    private String msgMontoMinimoVinculacion;

    @Value("${constantes.mensaje.apertura.previa}")
    private String msgAperturaPrevia;

    @Value("${constantes.mensaje.saldo.insuficiente}")
    private String msgSaldoInsuficiente;
    private final OperacionesMapper mapper;
    private final TransaccionRepository transaccionRepository;
    private final FondoServiceImpl fondoService;
    private final ClienteServiceImpl clienteService;
    private final NotificadorService notificadorService;

    private Transaccion transaccion;
    private Transaccion transaccionAperturaPrevia = null;


    public TransaccionServiceImpl(TransaccionRepository transaccionRepository, FondoServiceImpl fondoService, ClienteServiceImpl clienteService, NotificadorService notificadorService, OperacionesMapper mapper) {
        this.transaccionRepository = transaccionRepository;
        this.fondoService = fondoService;
        this.clienteService = clienteService;
        this.notificadorService = notificadorService;
        this.mapper = mapper;
    }

    public Page<TransaccionDto> consultarTransaccionesByCliente(
            String idCliente,
            OpcionesOrdenamientoTransEnum ordernarPor,
            int pageNo,
            int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(ordernarPor.dtoToEntity()).descending());
        var transacciones = transaccionRepository.findAllByIdCliente(idCliente, pageable);
        return new PageImpl<>(
                transacciones.getContent().stream()
                        .map(mapper::transaccionToTransaccionDto).toList(),
                pageable, transacciones.getTotalElements());

    }


    public Transaccion guardarTransaccion(TransaccionDto request) {
        this.transaccion = mapper.transaccionDtoToTransaccion(request);
        var transaccionesDelCLiente = transaccionRepository.findAllByIdCliente(transaccion.getIdCliente());
        var cliente = clienteService.consultarCliente(transaccion.getIdCliente());
        var fondo = fondoService.consultarFondo(transaccion.getIdFondo());
        validacionesCliente(cliente, fondo , transaccionesDelCLiente);
        validacionesFondo(fondo);
        Transaccion transCreada;
        try{
            transCreada = transaccionRepository.save(transaccion);
        }catch (Exception e){
            throw new ServidorException(msgErrorGuardarTransaccion);
        }
        actualizarSaldoFondo(fondo);
        actualizarSaldoCliente(cliente);
        envioNotificacion(cliente, fondo);
        return transCreada;
    }

    private void envioNotificacion(ClienteDto cliente, FondoDto fondo) {
        NotificacionDto notificacionDto = new NotificacionDto(cliente, fondo.getNombreFondo(), transaccion.getTipoTransaccion(), transaccion.getTipoNotificacion());
        notificadorService.notificar(notificacionDto);
    }


    private void validacionesFondo(FondoDto fondo) {
        if (fondo == null) {
            throw new ObjetoNoEncontradoException(msgFondoNoEncontrado);
        }
        if (transaccion.getTipoTransaccion().equals(TipoTransaccionEnum.APERTURAR.name()) && fondo.getMontoMinimo() > transaccion.getMonto()) {
            throw new RequestInvalidoException(msgMontoMinimoVinculacion+ fondo.getMontoMinimo());
        }
    }

    private void validacionesCliente(ClienteDto cliente, FondoDto fondo, List<Transaccion> transaccionesDelCLiente) {
        if (cliente == null) {
            throw new ObjetoNoEncontradoException(msgClienteNoEncontrado);

        }
        if (transaccion.getTipoTransaccion().equals(TipoTransaccionEnum.APERTURAR.name()) && cliente.getValorSaldo() < transaccion.getMonto()) {
            throw new RequestInvalidoException(msgSaldoInsuficiente + fondo.getNombreFondo());
        }
        transaccionAperturaPrevia = transaccionesDelCLiente.stream().filter(t -> t.getIdFondo().equals(fondo.getIdFondo()) && t.getTipoTransaccion().equals(TipoTransaccionEnum.APERTURAR.name())).findAny().orElse(null);
        if(transaccionAperturaPrevia != null && transaccion.getTipoTransaccion().equals(TipoTransaccionEnum.APERTURAR.name())){
            throw new RequestInvalidoException(msgAperturaPrevia + fondo.getNombreFondo());
        }
    }

    private void actualizarSaldoFondo(FondoDto fondo) {
        if (transaccion.getTipoTransaccion().equals(TipoTransaccionEnum.APERTURAR.name())) {
            fondo.setSaldoFondo(fondo.getSaldoFondo() + transaccion.getMonto());
            fondoService.actualizarFondo(fondo, fondo.getIdFondo());
        } else if (transaccion.getTipoTransaccion().equals(TipoTransaccionEnum.CANCELAR.name())) {
            fondo.setSaldoFondo(fondo.getSaldoFondo() - transaccionAperturaPrevia.getMonto());
            fondoService.actualizarFondo(fondo, fondo.getIdFondo());
        }
    }

    private void actualizarSaldoCliente(ClienteDto cliente) {
        if (transaccion.getTipoTransaccion().equals(TipoTransaccionEnum.APERTURAR.name())) {
            cliente.setValorSaldo(cliente.getValorSaldo() - transaccion.getMonto());
            clienteService.actualizarCliente(cliente, cliente.getIdCliente());
        } else if (transaccion.getTipoTransaccion().equals(TipoTransaccionEnum.CANCELAR.name())) {
            cliente.setValorSaldo(cliente.getValorSaldo() + transaccionAperturaPrevia.getMonto());
            clienteService.actualizarCliente(cliente, cliente.getIdCliente());
        }
    }


}
