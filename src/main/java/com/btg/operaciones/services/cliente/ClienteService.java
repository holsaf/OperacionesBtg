package com.btg.operaciones.services.cliente;

import com.btg.operaciones.dto.ClienteDto;

public interface ClienteService {

    ClienteDto consultarCliente(String id);

    ClienteDto crearCliente(ClienteDto cliente);

    double consultarSaldoCliente(String id);

    void actualizarCliente(ClienteDto cliente, String id);
}
