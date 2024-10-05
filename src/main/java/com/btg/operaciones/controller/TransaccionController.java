package com.btg.operaciones.controller;

import com.btg.operaciones.dto.TransaccionDto;
import com.btg.operaciones.enums.OpcionesOrdenamientoTransEnum;
import com.btg.operaciones.handlers.CustomExceptions.RequestInvalidoException;
import com.btg.operaciones.services.transaccion.TransaccionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transacciones")
public class TransaccionController {

    private final TransaccionService transaccionService;

    public TransaccionController(TransaccionService transaccionService){
        this.transaccionService = transaccionService;
    }

    @PostMapping()
    public ResponseEntity<?> crearTransaccion(@Valid @RequestBody TransaccionDto transaccionDto){
        var response = this.transaccionService.guardarTransaccion(transaccionDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{clienteId}/{ordernarPor}/{pageNo}/{pageSize}")
    public ResponseEntity<?> consultarTransaccionesByClienteAndFechaDesde(
            @PathVariable String clienteId,
            @PathVariable String ordernarPor,
            @PathVariable int pageNo,
            @PathVariable int pageSize)  {
        OpcionesOrdenamientoTransEnum ordenamiento;
        try{
            ordenamiento = OpcionesOrdenamientoTransEnum.valueOf(ordernarPor);
        }catch (IllegalArgumentException e){
            throw  new RequestInvalidoException("Opción de ordenamiento invalida");
        }
        var response = this.transaccionService.consultarTransaccionesByCliente(clienteId, ordenamiento,pageNo, pageSize);
        return ResponseEntity.ok(response);
    }


}
