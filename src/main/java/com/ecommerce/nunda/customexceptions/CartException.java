package com.ecommerce.nunda.customexceptions;

public class CartException extends RuntimeException {
    public CartException(String message) {
        super(message);
    }
}
