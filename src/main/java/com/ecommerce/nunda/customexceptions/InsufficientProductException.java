package com.ecommerce.nunda.customexceptions;

public class InsufficientProductException extends RuntimeException{
     public InsufficientProductException(String message){
        super(message);
    }
}
