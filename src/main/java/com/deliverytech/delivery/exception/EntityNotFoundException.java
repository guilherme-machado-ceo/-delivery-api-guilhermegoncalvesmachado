package com.deliverytech.delivery.exception;

public class EntityNotFoundException extends RuntimeException {
    
    public EntityNotFoundException(String message) {
        super(message);
    }
    
    public EntityNotFoundException(String entityName, Long id) {
        super(String.format("%s com ID %d não encontrado", entityName, id));
    }
    
    public EntityNotFoundException(String entityName, String field, Object value) {
        super(String.format("%s com %s '%s' não encontrado", entityName, field, value));
    }
}