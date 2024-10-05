package com.btg.operaciones.services.fondo;

import com.btg.operaciones.dto.FondoDto;

public interface FondoService {

    FondoDto crearFondo(FondoDto fondo);
    FondoDto consultarFondo(String id);

    void actualizarFondo(FondoDto dto, String id);


}
