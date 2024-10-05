package com.btg.operaciones.dto;

import lombok.Data;

@Data
public class NotificacionDto {

    private ClienteDto cliente;
    private String nombreFondo;
    private String tipoTransaccion;
    private String tipoNotificacion;

    public NotificacionDto(ClienteDto cliente, String nombreFondo, String tipoTransaccion, String tipoNotificacion) {
        this.cliente = cliente;
        this.nombreFondo = nombreFondo;
        this.tipoTransaccion = tipoTransaccion;
        this.tipoNotificacion = tipoNotificacion;
    }

}
