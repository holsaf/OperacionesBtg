package com.btg.operaciones.services;

import com.btg.operaciones.dto.ClienteDto;
import com.btg.operaciones.dto.FondoDto;
import com.btg.operaciones.dto.NotificacionDto;
import com.btg.operaciones.dto.TransaccionDto;
import com.btg.operaciones.entities.Transaccion;
import com.btg.operaciones.enums.OpcionesOrdenamientoTransEnum;
import com.btg.operaciones.enums.TipoNotificacionEnum;
import com.btg.operaciones.enums.TipoTransaccionEnum;
import com.btg.operaciones.handlers.CustomExceptions.ObjetoNoEncontradoException;
import com.btg.operaciones.handlers.CustomExceptions.RequestInvalidoException;
import com.btg.operaciones.handlers.CustomExceptions.ServidorException;
import com.btg.operaciones.mappers.OperacionesMapper;
import com.btg.operaciones.repository.TransaccionRepository;
import com.btg.operaciones.services.cliente.ClienteServiceImpl;
import com.btg.operaciones.services.fondo.FondoServiceImpl;
import com.btg.operaciones.services.notificador.NotificadorService;
import com.btg.operaciones.services.transaccion.TransaccionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransaccionServiceImplTest {

    @Mock
    private TransaccionRepository transaccionRepository;

    @Mock
    private FondoServiceImpl fondoService;

    @Mock
    private ClienteServiceImpl clienteService;

    @Mock
    private NotificadorService notificadorService;

    @Mock
    private OperacionesMapper mapper;

    @InjectMocks
    private TransaccionServiceImpl transaccionService;

    TransaccionDto request ;
    Transaccion transaccion ;
    ClienteDto cliente;
    FondoDto fondo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        request = createMockTransaccionDto();
        transaccion = createMockTransaccion();
        cliente = new ClienteDto();
        fondo = new FondoDto();
    }

    @Test
    void consultarTransaccionesByCliente_ReturnsPageOfTransacciones() {
        String idCliente = "123";
        OpcionesOrdenamientoTransEnum ordenarPor = OpcionesOrdenamientoTransEnum.valorMonto;
        int pageNo = 0;
        int pageSize = 10;
        var mockTransacciones = createMockTransacciones();
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(ordenarPor.dtoToEntity()).descending());
        Page<Transaccion> page = new PageImpl<>(mockTransacciones);

        when(transaccionRepository.findAllByIdCliente(any(String.class),any(Pageable.class))).thenReturn(page);

        Page<TransaccionDto> result = transaccionService.consultarTransaccionesByCliente(idCliente, ordenarPor, pageNo, pageSize);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        verify(transaccionRepository, times(1)).findAllByIdCliente(idCliente, pageable);
    }

    @Test
    void consultarTransaccionesByCliente_ReturnsEmptyPageWhenNoTransacciones() {
        String idCliente = "123";
        OpcionesOrdenamientoTransEnum ordenarPor = OpcionesOrdenamientoTransEnum.valorMonto;
        int pageNo = 0;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(ordenarPor.dtoToEntity()).descending());
        Page<Transaccion> page = Page.empty(pageable);

        when(transaccionRepository.findAllByIdCliente(idCliente, pageable)).thenReturn(page);

        Page<TransaccionDto> result = transaccionService.consultarTransaccionesByCliente(idCliente, ordenarPor, pageNo, pageSize);

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        verify(transaccionRepository, times(1)).findAllByIdCliente(idCliente, pageable);
    }

    @Test
    void consultarTransaccionesByCliente_ThrowsExceptionWhenRepositoryFails() {
        String idCliente = "123";
        OpcionesOrdenamientoTransEnum ordenarPor = OpcionesOrdenamientoTransEnum.valorMonto;
        int pageNo = 0;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(ordenarPor.dtoToEntity()).descending());

        when(transaccionRepository.findAllByIdCliente(idCliente, pageable)).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> transaccionService.consultarTransaccionesByCliente(idCliente, ordenarPor, pageNo, pageSize));
    }


    @Test
    void guardarTransaccion_SuccessfulTransaction() {
        List<Transaccion> transaccionesDelCliente = createMockTransacciones();

        when(mapper.transaccionDtoToTransaccion(request)).thenReturn(transaccion);
        when(transaccionRepository.findAllByIdCliente(transaccion.getIdCliente())).thenReturn(transaccionesDelCliente);
        when(clienteService.consultarCliente(transaccion.getIdCliente())).thenReturn(cliente);
        when(fondoService.consultarFondo(transaccion.getIdFondo())).thenReturn(fondo);
        when(transaccionRepository.save(transaccion)).thenReturn(transaccion);

        Transaccion result = transaccionService.guardarTransaccion(request);

        assertNotNull(result);
        verify(transaccionRepository, times(1)).save(transaccion);
        verify(clienteService, times(1)).consultarCliente(transaccion.getIdCliente());
        verify(fondoService, times(1)).consultarFondo(transaccion.getIdFondo());
        verify(notificadorService, times(1)).notificar(any(NotificacionDto.class));
    }

    @Test
    void guardarTransaccion_ThrowsServidorException() {
        List<Transaccion> transaccionesDelCliente = Collections.emptyList();

        when(mapper.transaccionDtoToTransaccion(request)).thenReturn(transaccion);
        when(transaccionRepository.findAllByIdCliente(transaccion.getIdCliente())).thenReturn(transaccionesDelCliente);
        when(clienteService.consultarCliente(transaccion.getIdCliente())).thenReturn(cliente);
        when(fondoService.consultarFondo(transaccion.getIdFondo())).thenReturn(fondo);
        when(transaccionRepository.save(transaccion)).thenThrow(new RuntimeException());

        assertThrows(ServidorException.class, () -> transaccionService.guardarTransaccion(request));
    }

    @Test
    void guardarTransaccion_ThrowsObjetoNoEncontradoException() {
        List<Transaccion> transaccionesDelCliente = Collections.emptyList();

        when(mapper.transaccionDtoToTransaccion(request)).thenReturn(transaccion);
        when(transaccionRepository.findAllByIdCliente(transaccion.getIdCliente())).thenReturn(transaccionesDelCliente);
        when(clienteService.consultarCliente(transaccion.getIdCliente())).thenReturn(null);

        assertThrows(ObjetoNoEncontradoException.class, () -> transaccionService.guardarTransaccion(request));
    }

    @Test
    void guardarTransaccion_ThrowsRequestInvalidoException() {
        List<Transaccion> transaccionesDelCliente = Collections.emptyList();

        when(mapper.transaccionDtoToTransaccion(request)).thenReturn(transaccion);
        when(transaccionRepository.findAllByIdCliente(transaccion.getIdCliente())).thenReturn(transaccionesDelCliente);
        when(clienteService.consultarCliente(transaccion.getIdCliente())).thenReturn(cliente);
        when(fondoService.consultarFondo(transaccion.getIdFondo())).thenReturn(fondo);
        cliente.setValorSaldo(100);
        fondo.setMontoMinimo(200);
        transaccion.setMonto(150);
        transaccion.setTipoTransaccion(TipoTransaccionEnum.APERTURAR.name());

        assertThrows(RequestInvalidoException.class, () -> transaccionService.guardarTransaccion(request));
    }


    @Test
    void guardarTransaccion_ThrowsRequestInvalidoExceptionWhenAperturaPreviaExists() {
        List<Transaccion> transaccionesDelCliente = createMockTransacciones();

        when(mapper.transaccionDtoToTransaccion(request)).thenReturn(transaccion);
        when(transaccionRepository.findAllByIdCliente(transaccion.getIdCliente())).thenReturn(transaccionesDelCliente);
        when(clienteService.consultarCliente(transaccion.getIdCliente())).thenReturn(cliente);
        when(fondoService.consultarFondo(transaccion.getIdFondo())).thenReturn(fondo);
        transaccion.setTipoTransaccion(TipoTransaccionEnum.APERTURAR.name());

        assertThrows(RequestInvalidoException.class, () -> transaccionService.guardarTransaccion(request));
    }

    @Test
    void guardarTransaccion_ThrowsRequestInvalidoExceptionWhenSaldoInsuficiente() {
        List<Transaccion> transaccionesDelCliente = Collections.emptyList();

        when(mapper.transaccionDtoToTransaccion(request)).thenReturn(transaccion);
        when(transaccionRepository.findAllByIdCliente(transaccion.getIdCliente())).thenReturn(transaccionesDelCliente);
        when(clienteService.consultarCliente(transaccion.getIdCliente())).thenReturn(cliente);
        when(fondoService.consultarFondo(transaccion.getIdFondo())).thenReturn(fondo);
        cliente.setValorSaldo(50);
        transaccion.setMonto(100);
        transaccion.setTipoTransaccion(TipoTransaccionEnum.APERTURAR.name());

        assertThrows(RequestInvalidoException.class, () -> transaccionService.guardarTransaccion(request));
    }

    public static Transaccion createMockTransaccion(){
        Transaccion transaccion = new Transaccion();
        transaccion.setId("1");
        transaccion.setUniqueId(UUID.randomUUID());
        transaccion.setIdCliente("cliente1");
        transaccion.setIdFondo("fondo1");
        transaccion.setTipoTransaccion("apertura");
        transaccion.setTipoNotificacion("email");
        transaccion.setMonto(1000.0);
        transaccion.setFecha(new Date());
        return transaccion;
    }
    public static List<Transaccion> createMockTransacciones (){
        List<Transaccion> mockTransacciones = new ArrayList<>();
        Transaccion transaccion1 = createMockTransaccion();
        Transaccion transaccion2 = createMockTransaccion();

        mockTransacciones.add(transaccion1);
        mockTransacciones.add(transaccion2);
        return mockTransacciones;
    }

    public static TransaccionDto createMockTransaccionDto(){
        TransaccionDto transaccionDto = new TransaccionDto();
        transaccionDto.setIdTransaccion("1");
        transaccionDto.setIdentificadorUnico(UUID.randomUUID().toString());
        transaccionDto.setIdCliente("cliente1");
        transaccionDto.setIdFondo("fondo1");
        transaccionDto.setTipoTransaccion(TipoTransaccionEnum.APERTURAR);
        transaccionDto.setTipoNotificacion(TipoNotificacionEnum.EMAIL);
        transaccionDto.setValorMonto(1000.0);
        transaccionDto.setFechaTransaccion(new Date());
        return  transaccionDto;
    }

    public static List<TransaccionDto> createMockTransaccionesDto (){
        List<TransaccionDto> mockTransacciones = new ArrayList<>();
        TransaccionDto transaccion1 = createMockTransaccionDto();
        TransaccionDto transaccion2 = createMockTransaccionDto();

        mockTransacciones.add(transaccion1);
        mockTransacciones.add(transaccion2);
        return mockTransacciones;
    }

}
