package com.btg.operaciones.services.transaccion;

import com.btg.operaciones.entities.Transaccion;
import com.btg.operaciones.dto.TransaccionDto;
import com.btg.operaciones.enums.OpcionesOrdenamientoTransEnum;
import org.springframework.data.domain.Page;

public interface TransaccionService {

    Page<TransaccionDto> consultarTransaccionesByCliente(
            String clienteId,
            OpcionesOrdenamientoTransEnum ordernarPor,
            int pageNo,
            int pageSize);

    Transaccion guardarTransaccion(TransaccionDto request);


}
