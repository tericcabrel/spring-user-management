package com.tericcabrel.authorization.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tericcabrel.authorization.models.response.BadRequestResponse;
import com.tericcabrel.authorization.models.response.InvalidDataResponse;
import com.tericcabrel.authorization.models.response.GenericResponse;
import com.tericcabrel.authorization.utils.Helpers;

@ControllerAdvice
public class GlobalExceptionHandler {

    private Map<String, String> formatMessage(String message) {
        Map<String, String> result = new HashMap<>();
        result.put("message", message);

        return result;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        BadRequestResponse response = new BadRequestResponse(formatMessage(ex.getMessage()));

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PasswordNotMatchException.class)
    public ResponseEntity<?> passwordNotMatchException(PasswordNotMatchException ex, WebRequest request) {
        BadRequestResponse response = new BadRequestResponse(formatMessage(ex.getMessage()));

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<?> fileNotFoundException(FileNotFoundException ex, WebRequest request) {
        BadRequestResponse response = new BadRequestResponse(formatMessage(ex.getMessage()));
        ex.printStackTrace();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<?> fileStorageException(FileStorageException ex, WebRequest request) {
        BadRequestResponse response = new BadRequestResponse(formatMessage(ex.getMessage()));
        ex.printStackTrace();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> constraintViolationException(ConstraintViolationException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();

        ex.getConstraintViolations().forEach(cv -> {
            String[] strings = cv.getPropertyPath().toString().split("\\.");

            errors.put(strings[strings.length - 1], cv.getMessage());
        });

        Map<String, Map<String,String>> result = new HashMap<>();
        result.put("errors", errors);

        GenericResponse response = new GenericResponse(result);

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

        Map<String, Map<String, List<String>>> result = new HashMap<>();
        result.put("errors", errors);

        InvalidDataResponse response = new InvalidDataResponse(result);

        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> accessDeniedException(AccessDeniedException ex, WebRequest request) {
        BadRequestResponse response = new BadRequestResponse(formatMessage(ex.getMessage()));

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> badCredentialsException(BadCredentialsException ex, WebRequest request) {
        BadRequestResponse response = new BadRequestResponse(formatMessage(ex.getMessage()));

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globalExceptionHandler(Exception ex, WebRequest request) {
        ex.printStackTrace();

        BadRequestResponse response = new BadRequestResponse(formatMessage(ex.getMessage()));

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
