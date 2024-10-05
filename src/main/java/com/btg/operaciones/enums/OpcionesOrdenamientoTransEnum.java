package com.btg.operaciones.enums;

public enum OpcionesOrdenamientoTransEnum {
    valorMonto,
    fechaTransaccion;

    public String dtoToEntity(){
        return switch (this) {
            case valorMonto -> "monto";
            case fechaTransaccion -> "fecha";
        };
    }

}
