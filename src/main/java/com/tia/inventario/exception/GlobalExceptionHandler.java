package com.tia.inventario.exception;
import com.tia.inventario.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
        log.warn("Error de negocio: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("400", ex.getMessage()));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        String errorMsg = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Error de validación");
        log.warn("Validación fallida: {}", errorMsg);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("400", errorMsg));
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        log.warn("Error en tiempo de ejecución: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("400", ex.getMessage()));
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("Error inesperado: ", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("500", "Error interno del servidor"));
    }
}