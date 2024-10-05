package com.btg.operaciones.entities;


import org.springframework.data.annotation.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "fondos")
public class Fondo {
    @Id
    private String id;

    @Indexed(unique=true)
    private String nombre;

    private String tipoFondo;

    private double montoMinimoApertura;

    private double saldoTotal;

}
