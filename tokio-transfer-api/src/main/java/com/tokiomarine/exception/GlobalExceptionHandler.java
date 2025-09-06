package com.tokiomarine.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {
        List<FieldErrorDTO> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fe -> new FieldErrorDTO(fe.getField(), fe.getDefaultMessage()))
                .collect(Collectors.toList());
        return ResponseEntity.badRequest().body(new ValidationErrorResponse(errors));
    }

    @ExceptionHandler({
            TransferDateBeforeTodayException.class,
            NoApplicableFeeException.class
    })
    public ResponseEntity<?> handleBusiness(RuntimeException ex, HttpServletRequest req) {
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new MessageResponse(ex.getMessage()));
    }

    static class MessageResponse {
        public final String message;
        MessageResponse(String message) { this.message = message; }
    }

    static class ValidationErrorResponse {
        public final List<FieldErrorDTO> errors;
        ValidationErrorResponse(List<FieldErrorDTO> errors) { this.errors = errors; }
    }

    static class FieldErrorDTO {
        public final String field;
        public final String message;
        FieldErrorDTO(String field, String message) {
            this.field = field;
            this.message = message;
        }
    }
}
