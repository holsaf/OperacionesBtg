package com.btg.operaciones.services.fondo;

import com.btg.operaciones.dto.FondoDto;
import com.btg.operaciones.entities.Fondo;
import com.btg.operaciones.handlers.CustomExceptions.ObjetoNoEncontradoException;
import com.btg.operaciones.handlers.CustomExceptions.ServidorException;
import com.btg.operaciones.mappers.OperacionesMapper;
import com.btg.operaciones.repository.FondoRepository;
import org.springframework.stereotype.Service;

@Service
public class FondoServiceImpl implements  FondoService{

    private static final String FONDO_NO_ENCONTRADO = "Fondo no encontrado";
    private static final String ERROR_CREAR_FONDO = "Error al crear el fondo";
    private static final String ERROR_ACTUALIZAR_FONDO = "Error al actualizar el fondo";
    private final FondoRepository fondoRepository;

    private final OperacionesMapper mapper;

    public FondoServiceImpl(FondoRepository fondoRepository, OperacionesMapper mapper){
        this.fondoRepository = fondoRepository;
        this.mapper = mapper;
    }

    public FondoDto consultarFondo(String fondoId) {

        var fondo = fondoRepository.findById(fondoId).orElse(null);
        if (fondo == null) {
            throw new ObjetoNoEncontradoException(FONDO_NO_ENCONTRADO);
        }
        return mapper.fondoToFondoDto(fondo);
    }

    public FondoDto crearFondo(FondoDto fondo) {
        var fondoNuevo = mapper.fondoDtoToFondo(fondo);
        Fondo fondoCreado;
        try {
            fondoCreado = fondoRepository.save(fondoNuevo);
        }catch (Exception e){
            throw new ServidorException(ERROR_CREAR_FONDO);
        }
        return mapper.fondoToFondoDto(fondoCreado);
    }
    public void actualizarFondo(FondoDto dto, String id) {
        var fondo = mapper.fondoDtoToFondo(dto);
        var fondoActual = fondoRepository.findById(id).orElse(null);
        if (fondoActual == null) {
            throw new ObjetoNoEncontradoException(FONDO_NO_ENCONTRADO);
        }
        try{
            fondoActual.setSaldoTotal(fondo.getSaldoTotal());
            fondoActual.setNombre(fondo.getNombre());
            fondoActual.setMontoMinimoApertura(fondo.getMontoMinimoApertura());
            fondoRepository.save(fondoActual);
        }catch (Exception e) {
            throw new ServidorException(ERROR_ACTUALIZAR_FONDO);
        }
    }
}
