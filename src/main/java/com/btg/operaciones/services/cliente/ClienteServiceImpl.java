package com.btg.operaciones.services.cliente;

import com.btg.operaciones.dto.ClienteDto;
import com.btg.operaciones.entities.Cliente;
import com.btg.operaciones.handlers.CustomExceptions.ObjetoNoEncontradoException;
import com.btg.operaciones.handlers.CustomExceptions.ServidorException;
import com.btg.operaciones.mappers.OperacionesMapper;
import com.btg.operaciones.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class ClienteServiceImpl implements ClienteService {

    @Value("${constantes.mensaje.error.actualizar.cliente}")
    private String errorActualizarCliente;

    @Value("${constantes.mensaje.error.crear.cliente}")
    private String errorCrearCliente;

    @Value("${constantes.mensaje.cliente.noEcontrado}")
    private String clienteNoEncontrado;
    private final ClienteRepository clienteRepository;

    private final OperacionesMapper mapper;

    public ClienteServiceImpl(ClienteRepository clienteRepository, OperacionesMapper mapper) {
        this.clienteRepository = clienteRepository;
        this.mapper = mapper;
    }

    public ClienteDto consultarCliente(String id) {

        var cliente = this.clienteRepository.findById(id).orElse(null);
        if (cliente == null) {
            throw new ObjetoNoEncontradoException(clienteNoEncontrado);
        }
        return this.mapper.clienteToClienteDto(cliente);
    }

    public ClienteDto  crearCliente(ClienteDto cliente) {
        var clienteNuevo = this.mapper.clienteDtoToCliente(cliente);
        Cliente clienteCreado = null;
        try {
            clienteCreado = this.clienteRepository.save(clienteNuevo);
        } catch (Exception e) {
            throw new ServidorException(errorCrearCliente);
        }
        return this.mapper.clienteToClienteDto(clienteCreado);
    }
    public double consultarSaldoCliente(String id) {
        var cliente = this.clienteRepository.findById(id).orElse(null);
        if (cliente == null) {
            throw new ObjetoNoEncontradoException(clienteNoEncontrado);
        }
        return cliente.getSaldo();
    }

    public void actualizarCliente(ClienteDto clienteDto, String id) {
        var clienteActual = clienteRepository.findById(id).orElse(null);
        if (clienteActual == null) {
            throw new ObjetoNoEncontradoException(clienteNoEncontrado);
        }
        try {
            var cliente = this.mapper.clienteDtoToCliente(clienteDto);
            clienteActual.setNombre(cliente.getNombre());
            clienteActual.setEmail(cliente.getEmail());
            clienteActual.setTelefono(cliente.getTelefono());
            clienteActual.setSaldo(cliente.getSaldo());
            clienteRepository.save(clienteActual);
        } catch (Exception e) {
            throw new ServidorException(errorActualizarCliente);
        }


    }
}
