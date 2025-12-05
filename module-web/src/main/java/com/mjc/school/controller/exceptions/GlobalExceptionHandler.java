package com.mjc.school.controller.exceptions;

import com.mjc.school.service.exceptions.ResourceNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(ResourceNotFoundException rnfe, WebRequest request){
        return ResponseEntity.status(NOT_FOUND)
                .body(new ApiError("Resource not found","NOT_FOUND_ERROR", Instant.now(),path(request), Map.of("id",rnfe.getMessage())));
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class, IllegalArgumentException.class})
    public ResponseEntity<ApiError> handleValidation(Exception e, WebRequest request){
        Map<String,String> errors = new LinkedHashMap<>();

        if(e instanceof MethodArgumentNotValidException m){
            m.getBindingResult().getFieldErrors()
                    .forEach(error->errors.put(error.getField(),error.getDefaultMessage()));
        } else if(e instanceof ConstraintViolationException c){
            c.getConstraintViolations()
                    .forEach(error->errors.put(error.getPropertyPath().toString(),error.getMessage()));
        }else{
            errors.put("request",e.getMessage());
        }
        return ResponseEntity.badRequest()
                .body(new ApiError("Validation failed","VALIDATION_ERROR",Instant.now(),path(request),errors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAll(Exception e, WebRequest request){
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(new ApiError("Server error","INTERNAL_SERVER_ERROR",Instant.now(),path(request),null));
    }

    private String path(WebRequest request){
        return request.getDescription(false).replace("uri=","");
    }

}
