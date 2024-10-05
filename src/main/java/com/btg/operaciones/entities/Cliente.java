package com.btg.operaciones.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Data
@Document(collection = "clientes")
public class Cliente {

    @Id
    private String id;

    private String nombre;

    //No se configura unique para poder usa el correo personal.
    private String email;

    //No se configura unique para poder usa el numeroa sinado por Twilio
    private String telefono;

    private double saldo;


}