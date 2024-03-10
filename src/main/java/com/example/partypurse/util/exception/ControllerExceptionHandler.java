package com.example.partypurse.util.exception;

import com.example.partypurse.util.errors.*;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<String> handleHttpClientErrorException(HttpClientErrorException ex) {
        HttpStatusCode status = ex.getStatusCode();
        String errorMessage = ex.getMessage();
        return ResponseEntity
                .status(status)
                .body(errorMessage);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        HttpStatusCode status = ex.getStatusCode();
        ex.getBindingResult().getAllErrors().forEach(
                error -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                });
        return ResponseEntity
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errors);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put(ex.getName(), "Вы указали неправильный тип данных");
        return ResponseEntity
                .badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleNotReadableMessage(HttpMessageNotReadableException ex) {
        return ResponseEntity
                .badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body("Ошибка в теле запроса");
    }

    @ExceptionHandler(PasswordComplexityException.class)
    public ResponseEntity<String> handlePasswordComplexity(PasswordComplexityException ex) {
        return ResponseEntity
                .badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body("Пароль не проходит тербования");
    }

    @ExceptionHandler(PasswordNotEqualsException.class)
    public ResponseEntity<String> handlePasswordComplexity(PasswordNotEqualsException ex) {
        return ResponseEntity
                .badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body("Пароли не совпадают");
    }

    @ExceptionHandler(SamePasswordEqualsWhenUpdateException.class)
    public ResponseEntity<String> handlePasswordComplexity(SamePasswordEqualsWhenUpdateException ex) {
        return ResponseEntity
                .badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body("Новый пароль должен отличаться от старого");
    }

    @ExceptionHandler(RoomAccessException.class)
    public ResponseEntity<String> handlePasswordComplexity(RoomAccessException ex) {
        return ResponseEntity
                .badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body("У вас нет прав на изменение данной комнаты");
    }

    @ExceptionHandler(ProductModifyException.class)
    public ResponseEntity<String> handlePasswordComplexity(ProductModifyException ex) {
        return ResponseEntity
                .badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body("У вас нет прав на изменение данного продукта");
    }


    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> handlePasswordComplexity(IOException ex) {
        return ResponseEntity
                .badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body("Неверный путь переадресации");
    }

}
