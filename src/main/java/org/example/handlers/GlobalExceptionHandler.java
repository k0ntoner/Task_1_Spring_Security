package org.example.handlers;


import jakarta.persistence.PersistenceException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(PersistenceException.class)
    public ResponseEntity<?> handlePersistenceException(PersistenceException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String,String>> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(constraintViolation -> {errors.put(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage());});

        return ResponseEntity.badRequest().body(errors);
    }
    @ExceptionHandler(ConversionFailedException.class)
    public ResponseEntity<?> handleConstraintViolation(ConversionFailedException ex) {
        if(ex.getCause() instanceof ConstraintViolationException) {
            return handleConstraintViolation((ConstraintViolationException) ex.getCause());
        }
        return ResponseEntity.badRequest().body(ex.getCause().getMessage());
    }
    @ExceptionHandler(InvocationTargetException.class)
    public ResponseEntity<?> handleInvocationTargetException(InvocationTargetException ex) {
        if (ex.getCause() instanceof ConstraintViolationException) {
            handleConstraintViolation((ConstraintViolationException) ex.getCause());
        }
        return ResponseEntity.badRequest().body(ex.getMessage());
    }



}
