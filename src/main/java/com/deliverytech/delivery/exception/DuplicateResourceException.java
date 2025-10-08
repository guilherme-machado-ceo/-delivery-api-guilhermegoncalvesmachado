package com.deliverytech.delivery.exception;

public class DuplicateResourceException extends RuntimeException {
    
    public DuplicateResourceException(String message) {
        super(message);
    }
    
    public DuplicateResourceException(String resourceName, String field, Object value) {
        super(String.format("%s com %s '%s' já existe", resourceName, field, value));
    }
}