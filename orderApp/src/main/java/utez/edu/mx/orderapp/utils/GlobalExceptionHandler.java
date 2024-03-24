package utez.edu.mx.orderapp.utils;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Response<Object>> handleRuntimeException(RuntimeException ex) {
        Response<Object> response = new Response<>(true, 400, "Error: " + ex.getMessage());
        return ResponseEntity.badRequest().body(response);
    }
}