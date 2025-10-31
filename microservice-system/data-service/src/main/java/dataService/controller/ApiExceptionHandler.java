package dataService.controller;

import dataService.exception.CategoriaNoEncontradaException;
import dataService.exception.ProductoNoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ProductoNoEncontradoException.class)
    public ResponseEntity<?> handleProductoNoEncontrado(ProductoNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("timestamp", Instant.now(), "error", ex.getMessage()));
    }

    @ExceptionHandler(CategoriaNoEncontradaException.class)
    public ResponseEntity<?> handleCategoriaNoEncontrada(CategoriaNoEncontradaException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("timestamp", Instant.now(), "error", ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("timestamp", Instant.now(), "error", ex.getMessage()));
    }
}
