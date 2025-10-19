package com.meli.ordermanagementsystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Esta clase atrapa excepciones de todos los controladores.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Este método se dispara CADA VEZ que un controlador lanza la excepción
     * MethodArgumentNotValidException (que es la que lanza @Valid).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // Asegura que la respuesta siempre sea 400
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        // Recorremos todos los errores de campo que encontró la validación
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            // Y añadimos el nombre del campo y el mensaje de error al mapa
            errors.put(error.getField(), error.getDefaultMessage());
        });

        return errors;
    }
}