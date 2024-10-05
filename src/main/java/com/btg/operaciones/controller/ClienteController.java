package com.btg.operaciones.controller;

import com.btg.operaciones.dto.ClienteDto;
import com.btg.operaciones.services.cliente.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService){
        this.clienteService = clienteService;
    }

    @PostMapping()
    public ResponseEntity<?> crearCliente(@Valid @RequestBody ClienteDto body){
        var response = this.clienteService.crearCliente(body);
        return ResponseEntity.ok(response);
    }

}
