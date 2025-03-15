package com.bookhub.CustomException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
//    @ExceptionHandler(UnauthorizedActionException.class)
//    public ResponseEntity<Map<String, String>> handleUnauthorizedActionException(UnauthorizedActionException ex) {
//        Map<String, String> response = new HashMap<>();
//        response.put("error", "Unauthorized");
//        response.put("message", ex.getMessage());
//        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
//    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Internal Server Error");
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseEntity<Map<String, String>> handleEmailAlreadyExistException(EmailAlreadyExistException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Email Already Exist");
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UnauthorizedActionException.class)
    public ResponseEntity<String> handleUnauthorizedActionException(UnauthorizedActionException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }
}
