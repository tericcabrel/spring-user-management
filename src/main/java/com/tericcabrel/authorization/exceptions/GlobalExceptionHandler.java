package com.tericcabrel.authorization.exceptions;

import com.tericcabrel.authorization.utils.Helpers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolationException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.tericcabrel.authorization.models.response.ServiceResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    private HashMap<String, String> formatMessage(String message) {
        HashMap<String, String> result = new HashMap<>();
        result.put("message", message);

        return result;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ServiceResponse response = new ServiceResponse(HttpStatus.NOT_FOUND.value(), formatMessage(ex.getMessage()));

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PasswordNotMatchException.class)
    public ResponseEntity<?> passwordNotMatchException(PasswordNotMatchException ex, WebRequest request) {
        ServiceResponse response = new ServiceResponse(HttpStatus.BAD_REQUEST.value(), formatMessage(ex.getMessage()));

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<?> fileNotFoundException(FileNotFoundException ex, WebRequest request) {
        ServiceResponse response = new ServiceResponse(HttpStatus.NOT_FOUND.value(), formatMessage(ex.getMessage()));
        ex.printStackTrace();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<?> fileStorageException(FileStorageException ex, WebRequest request) {
        ServiceResponse response = new ServiceResponse(HttpStatus.BAD_REQUEST.value(), formatMessage(ex.getMessage()));
        ex.printStackTrace();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> constraintViolationException(ConstraintViolationException ex, WebRequest request) {
        HashMap<String, String> errors = new HashMap<>();

        ex.getConstraintViolations().forEach(cv -> {
            String[] strings = cv.getPropertyPath().toString().split("\\.");

            errors.put(strings[strings.length - 1], cv.getMessage());
        });

        HashMap<String, HashMap<String,String>> result = new HashMap<>();
        result.put("errors", errors);

        ServiceResponse response = new ServiceResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(), result);

        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> methodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
        HashMap<String, List<String>> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(objectError -> {
            String field = "";

            if (objectError.getArguments() != null && objectError.getArguments().length >= 2) {
                field = objectError.getArguments()[1].toString();
            }

            if (field.length() > 0) {
                Helpers.updateErrorHashMap(errors, field, objectError.getDefaultMessage());
            }
        });

        ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
            Helpers.updateErrorHashMap(errors, fieldError.getField(), fieldError.getDefaultMessage());
        });

        HashMap<String, HashMap<String, List<String>>> result = new HashMap<>();
        result.put("errors", errors);

        ServiceResponse response = new ServiceResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(), result);

        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> accessDeniedException(AccessDeniedException ex, WebRequest request) {
        ServiceResponse response = new ServiceResponse(HttpStatus.FORBIDDEN.value(), formatMessage(ex.getMessage()));

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> badCredentialsException(BadCredentialsException ex, WebRequest request) {
        ServiceResponse response = new ServiceResponse(HttpStatus.UNAUTHORIZED.value(), formatMessage(ex.getMessage()));

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globalExceptionHandler(Exception ex, WebRequest request) {
        ex.printStackTrace();

        ServiceResponse response = new ServiceResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), formatMessage(ex.getMessage()));

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
