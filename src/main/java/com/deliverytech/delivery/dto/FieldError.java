package com.deliverytech.delivery.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Erro de campo específico")
public class FieldError {
    
    @Schema(description = "Nome do campo com erro", example = "nome")
    private String field;
    
    @Schema(description = "Valor rejeitado", example = "")
    private Object rejectedValue;
    
    @Schema(description = "Mensagem de erro do campo", example = "Nome é obrigatório")
    private String message;
    
    public FieldError() {}
    
    public FieldError(String field, String message) {
        this.field = field;
        this.message = message;
    }
    
    public FieldError(String field, Object rejectedValue, String message) {
        this.field = field;
        this.rejectedValue = rejectedValue;
        this.message = message;
    }
    
    // Getters and Setters
    public String getField() {
        return field;
    }
    
    public void setField(String field) {
        this.field = field;
    }
    
    public Object getRejectedValue() {
        return rejectedValue;
    }
    
    public void setRejectedValue(Object rejectedValue) {
        this.rejectedValue = rejectedValue;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
}