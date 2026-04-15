package com.its.gestionale.exception;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler({AziendaNotFoundException.class, CorsoNotFoundException.class})
    public ResponseEntity<ApiErrorResponse> handleNotFound(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class, IllegalArgumentException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ApiErrorResponse> handleBadRequest(Exception exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiErrorResponse(extractMessage(exception)));
    }

    private String extractMessage(Exception exception) {
        if (exception instanceof MethodArgumentNotValidException validationException) {
            List<FieldError> fieldErrors = validationException.getBindingResult().getFieldErrors();
            if (!fieldErrors.isEmpty()) {
                return fieldErrors.get(0).getDefaultMessage();
            }
        }

        if (exception instanceof ConstraintViolationException constraintViolationException && !constraintViolationException.getConstraintViolations().isEmpty()) {
            return constraintViolationException.getConstraintViolations().iterator().next().getMessage();
        }

        return exception.getMessage();
    }

    public record ApiErrorResponse(String message) {
    }
}