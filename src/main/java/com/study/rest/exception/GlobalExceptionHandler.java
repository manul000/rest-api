package com.study.rest.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<?> notFound(TaskNotFoundException e) {
        return ResponseEntity.status(404)
                .body(
                        Map.of(
                                "timestamp",
                                LocalDateTime.now(),
                                "status",
                                404,
                                "message",
                                e.getMessage(),
                                "path",
                                "/tasks/" + e.getTaskId()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> validation(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest()
                .body(
                        Map.of(
                                "timestamp",
                                LocalDateTime.now(),
                                "status",
                                400,
                                "error",
                                "Bad Request",
                                "message",
                                "Validation failed",
                                "errors",
                                e.getBindingResult().getFieldErrors().stream()
                                        .map(
                                                f ->
                                                        Map.of(
                                                                "field",
                                                                f.getField(),
                                                                "message",
                                                                Objects.requireNonNullElse(f.getDefaultMessage(), "Invalid")))
                                        .toList()));
    }

    @ExceptionHandler({
            HttpMessageNotReadableException.class,
            MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<?> malformed(Exception e) {
        return ResponseEntity.badRequest().body(Map.of("status", 400, "message", "Malformed request"));
    }
}
