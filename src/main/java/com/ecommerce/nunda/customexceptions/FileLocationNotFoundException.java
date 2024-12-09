package com.ecommerce.nunda.customexceptions;


public class FileLocationNotFoundException extends EmptyFileException{
    public FileLocationNotFoundException(String message) {
        super(message);
    }

    public FileLocationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
