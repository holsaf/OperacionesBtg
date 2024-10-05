package com.btg.operaciones.controller;

import com.btg.operaciones.dto.FondoDto;
import com.btg.operaciones.services.fondo.FondoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fondos")
public class FondoController {

    private final FondoService fondoService;

    public FondoController(FondoService fondoService){
        this.fondoService = fondoService;
    }

    @PostMapping()
    public ResponseEntity<?> crearFondo(@Valid @RequestBody FondoDto body){
        var response = this.fondoService.crearFondo(body);
        return ResponseEntity.ok(response);
    }


}
