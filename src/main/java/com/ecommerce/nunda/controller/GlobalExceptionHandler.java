package com.ecommerce.nunda.controller;


import com.ecommerce.nunda.customexceptions.ProductNotFoundException;
import com.ecommerce.nunda.customexceptions.UserNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<?> handleProductNotFoundException(ProductNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<?> handleJsonProcessingException(JsonProcessingException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND));
    }



    /*
    * Helper method to create a structured error response
    *
    * @return Map<String, Object> errorResponse
    *        {
             "status": 404,
             "message": "Product not found or inactive"
              }
    *
    *
    * */
    private Map<String, String > createErrorResponse(String message, HttpStatus status) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("status", String.valueOf(status.value()));
        errorResponse.put("message", message);
        return errorResponse;
    }





}
