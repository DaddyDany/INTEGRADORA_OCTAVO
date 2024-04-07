package utez.edu.mx.orderapp.utils;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        ErrorResponse errorResponse = new ErrorResponse("Error de integridad de la informacion, es posible que alguno de los datos unicos ya este en uso", HttpStatus.CONFLICT.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        String errorMessage = ex.getMessage() != null ? ex.getMessage() : "Parámetros incorrectos";
        return new ResponseEntity<>(new Response<>(true, 420, errorMessage), HttpStatus.valueOf(CustomHttpStatus.HTTP_STATUS_420.value()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
        List<String> errorMessages = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errorMessages.add(violation.getMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessages);
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<?> handleTransactionSystemException(TransactionSystemException ex) {
        String errorMessage = "Error de transaccion, verifica que no hayas dejado campos vacios y que los valores sean correctos";
        return new ResponseEntity<>(new Response<>(true, 419, errorMessage), HttpStatus.valueOf(CustomHttpStatusTwo.HTTP_STATUS_419.value()));
    }

    static class ErrorResponse {
        private String message;
        private int status;

        public ErrorResponse(String message, int status) {
            this.message = message;
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public int getStatus() {
            return status;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }

    public enum CustomHttpStatus {
        HTTP_STATUS_420(420, "Correo duplicado");


        private final int value;
        private final String reasonPhrase;

        CustomHttpStatus(int value, String reasonPhrase) {
            this.value = value;
            this.reasonPhrase = reasonPhrase;
        }

        public int value() {
            return this.value;
        }

        public String getReasonPhrase() {
            return this.reasonPhrase;
        }
    }

    public enum CustomHttpStatusTwo {
        HTTP_STATUS_419(419, "Error de transaccion");


        private final int value;
        private final String reasonPhrase;

        CustomHttpStatusTwo(int value, String reasonPhrase) {
            this.value = value;
            this.reasonPhrase = reasonPhrase;
        }

        public int value() {
            return this.value;
        }

        public String getReasonPhrase() {
            return this.reasonPhrase;
        }
    }
}
