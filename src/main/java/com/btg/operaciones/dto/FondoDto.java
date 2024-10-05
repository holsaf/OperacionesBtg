package com.btg.operaciones.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class FondoDto {

    private String idFondo;

    @NotEmpty
    private String nombreFondo;

    @NotEmpty
    @Size(max = 3, message = "El tamaño del fondo debe ser maximo de 3 caracteres")
    private String tipoFondo;

    @DecimalMin(value="10000.00", message = "El monto minimo debe ser mayor a 10000")
    private double montoMinimo;

    private double saldoFondo;

}
