package com.adarshgs.demo.exceptions;

import java.util.HashMap;
import java.util.Map;

import com.adarshgs.demo.dtos.ErrorResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.adarshgs.demo.dtos.BindingErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<BindingErrorResponse> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException ex
    )
    {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().stream()
            .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BindingErrorResponse(HttpStatus.BAD_REQUEST.toString(), errors));
    }

    @ExceptionHandler(PublisherNotFound.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handlePublisherNotFoundException(
        PublisherNotFound ex
    )
    {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), ex.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation (
        DataIntegrityViolationException ex
    )
    {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), ex.getMessage()));
    }
}
