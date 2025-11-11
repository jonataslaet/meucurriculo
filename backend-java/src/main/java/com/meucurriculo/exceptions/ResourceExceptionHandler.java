package com.meucurriculo.exceptions;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.jspecify.annotations.NonNull;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<@NonNull StandardError> fileIllegalArgumentException(
            IllegalArgumentException ex, HttpServletRequest httpServletRequest) {
        StandardError standardError = new StandardError();
        standardError.setTimestamp(Instant.now());
        standardError.setStatus(HttpStatus.BAD_REQUEST.value());
        standardError.setError("Erro de requisição");
        standardError.setMessage(ex.getMessage());
        standardError.setPath(httpServletRequest.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(standardError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<@NonNull ValidationError> methodArgumentNotValidException(
            MethodArgumentNotValidException ex, HttpServletRequest httpServletRequest) {
        ValidationError validationError = new ValidationError();
        validationError.setTimestamp(Instant.now());
        validationError.setStatus(HttpStatus.UNPROCESSABLE_CONTENT.value());
        validationError.setError("Erro de validação");
        validationError.setMessage(ex.getMessage());
        validationError.setPath(httpServletRequest.getRequestURI());
        for (FieldError fieldError: ex.getBindingResult().getFieldErrors()) {
            validationError.addCustomFieldError(new CustomFieldError(fieldError.getField(), fieldError.getDefaultMessage()));
        }
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT.value()).body(validationError);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<@NonNull StandardError> handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest httpServletRequest) {
        StandardError standardError = new StandardError();
        standardError.setTimestamp(Instant.now());
        standardError.setStatus(HttpStatus.NOT_FOUND.value());
        standardError.setError("Recurso não encontrado");
        standardError.setMessage(ex.getMessage());
        standardError.setPath(httpServletRequest.getRequestURI());

        return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(standardError);
    }
}
