package com.btg.operaciones.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;

@Data
public class ClienteDto {

    private String idCliente;

    @NotEmpty
    @Size(min = 3, max = 50, message = "El nombre del cliente debe tener entre 3 y 50 caracteres")
    private String nombreCliente;

    @NotEmpty
    @Email(message = "El correo electrónico no es válido")
    private String correoElectronico;

    //validate international phone number
    @Pattern(regexp = "^(\\+\\d{1,3}[- ]?)?\\d{10}$", message = "El número de celular no es válido")
    private String numeroCelular;

    @DecimalMin(value="500000.00",  message = "El saldo inicial debe ser mayor o igual a 500000.0")
    private double valorSaldo;

}
