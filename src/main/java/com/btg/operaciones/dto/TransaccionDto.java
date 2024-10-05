package com.btg.operaciones.dto;

import com.btg.operaciones.enums.TipoNotificacionEnum;
import com.btg.operaciones.enums.TipoTransaccionEnum;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.Date;

@Data
public class TransaccionDto {

    private String idTransaccion;

    private String identificadorUnico;

    @NotEmpty
    private String idCliente;

    @NotEmpty
    private String idFondo;

    private TipoTransaccionEnum tipoTransaccion;

    private TipoNotificacionEnum tipoNotificacion;

    private double valorMonto;

    private Date fechaTransaccion;

}
