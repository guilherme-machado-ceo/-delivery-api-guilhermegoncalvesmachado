package com.deliverytech.delivery.exception;

/**
 * Exceção lançada quando uma entidade não é encontrada
 * Resulta em HTTP 404 Not Found
 */
public class EntityNotFoundException extends RuntimeException {
    
    private final String entityName;
    private final Object entityId;
    private final String errorCode;

    public EntityNotFoundException(String message) {
        super(message);
        this.entityName = "Entity";
        this.entityId = null;
        this.errorCode = "ENTITY_NOT_FOUND";
    }

    public EntityNotFoundException(String entityName, Object entityId) {
        super(String.format("%s não encontrado(a) com ID: %s", entityName, entityId));
        this.entityName = entityName;
        this.entityId = entityId;
        this.errorCode = "ENTITY_NOT_FOUND";
    }

    public EntityNotFoundException(String entityName, Object entityId, String message) {
        super(message);
        this.entityName = entityName;
        this.entityId = entityId;
        this.errorCode = "ENTITY_NOT_FOUND";
    }

    public EntityNotFoundException(String entityName, Object entityId, Throwable cause) {
        super(String.format("%s não encontrado(a) com ID: %s", entityName, entityId), cause);
        this.entityName = entityName;
        this.entityId = entityId;
        this.errorCode = "ENTITY_NOT_FOUND";
    }

    public String getEntityName() {
        return entityName;
    }

    public Object getEntityId() {
        return entityId;
    }

    public String getErrorCode() {
        return errorCode;
    }

    // Factory methods para entidades específicas

    public static EntityNotFoundException cliente(Long id) {
        return new EntityNotFoundException("Cliente", id);
    }

    public static EntityNotFoundException restaurante(Long id) {
        return new EntityNotFoundException("Restaurante", id);
    }

    public static EntityNotFoundException produto(Long id) {
        return new EntityNotFoundException("Produto", id);
    }

    public static EntityNotFoundException pedido(Long id) {
        return new EntityNotFoundException("Pedido", id);
    }

    public static EntityNotFoundException usuario(Long id) {
        return new EntityNotFoundException("Usuário", id);
    }

    public static EntityNotFoundException clientePorEmail(String email) {
        return new EntityNotFoundException("Cliente", email, "Cliente não encontrado com email: " + email);
    }

    public static EntityNotFoundException restaurantePorNome(String nome) {
        return new EntityNotFoundException("Restaurante", nome, "Restaurante não encontrado com nome: " + nome);
    }

    public static EntityNotFoundException produtoPorNome(String nome, Long restauranteId) {
        return new EntityNotFoundException("Produto", nome, 
            String.format("Produto '%s' não encontrado no restaurante ID: %d", nome, restauranteId));
    }
}