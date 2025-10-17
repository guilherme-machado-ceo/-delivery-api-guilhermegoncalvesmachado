package com.deliverytech.delivery.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Detalhes do erro")
public class ErrorDetails {
    
    @Schema(description = "Código do erro", example = "ENTITY_NOT_FOUND")
    private String code;
    
    @Schema(description = "Mensagem principal do erro", example = "Restaurante não encontrado")
    private String message;
    
    @Schema(description = "Detalhes adicionais do erro", example = "Nenhum restaurante encontrado com ID: 999")
    private String details;
    
    @Schema(description = "Lista de erros de campo (para validações)")
    private List<FieldError> fields;
    
    public ErrorDetails() {}
    
    public ErrorDetails(String code, String message) {
        this.code = code;
        this.message = message;
    }
    
    public ErrorDetails(String code, String message, String details) {
        this.code = code;
        this.message = message;
        this.details = details;
    }
    
    public ErrorDetails(String code, String message, List<FieldError> fields) {
        this.code = code;
        this.message = message;
        this.fields = fields;
    }
    
    // Getters and Setters
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getDetails() {
        return details;
    }
    
    public void setDetails(String details) {
        this.details = details;
    }
    
    public List<FieldError> getFields() {
        return fields;
    }
    
    public void setFields(List<FieldError> fields) {
        this.fields = fields;
    }
}