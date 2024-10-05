package com.btg.operaciones.entities;

import org.springframework.data.annotation.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.UUID;

@Data
@Document(collection = "transacciones")
public class Transaccion {
    @Id
    private String id;
    private UUID uniqueId;
    private String idCliente;
    private String idFondo;
    private String tipoTransaccion; // "apertura" o "cancelacion"
    private String tipoNotificacion; // "email" o "sms"
    private double monto;
    private Date fecha;


}