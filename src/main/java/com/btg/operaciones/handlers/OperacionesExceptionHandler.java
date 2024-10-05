package com.btg.operaciones.handlers;

import com.btg.operaciones.handlers.CustomExceptions.RequestInvalidoException;
import com.btg.operaciones.handlers.CustomExceptions.ObjetoNoEncontradoException;
import com.btg.operaciones.handlers.CustomExceptions.ServidorException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class OperacionesExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ObjetoNoEncontradoException.class)
    public ResponseEntity<String> handleObjetoNoEncontradoException(ObjetoNoEncontradoException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RequestInvalidoException.class)
    public ResponseEntity<String> handleRequestInvalidoException(RequestInvalidoException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ServidorException.class)
    public ResponseEntity<String> handleServidorException(ServidorException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleOtherExceptions(Exception ex) {
        return new ResponseEntity<>("Ocurrio un error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
