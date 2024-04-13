package utez.edu.mx.orderapp.utils;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        ErrorResponse errorResponse = new ErrorResponse("Error de integridad de la información", HttpStatus.CONFLICT.value());
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


    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex) {
        String errorMessage = "El tamaño del archivo es superior al permitido";
        return new ResponseEntity<>(new Response<>(true, 413, errorMessage), HttpStatus.valueOf(413));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        if (ex.getMessage().contains("Token invalido o expirado")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocurrió un error interno.");
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
        HTTP_STATUS_419(419, "Error de transacción");


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
