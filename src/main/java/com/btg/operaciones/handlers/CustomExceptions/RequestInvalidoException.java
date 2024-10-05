package com.btg.operaciones.handlers.CustomExceptions;

public class RequestInvalidoException extends RuntimeException {

    public RequestInvalidoException(String message) {
        super(message);
    }
}
