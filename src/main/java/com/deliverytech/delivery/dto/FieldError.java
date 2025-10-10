package com.deliverytech.delivery.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Erro específico de um campo")
public class FieldError {
    
    @Schema(description = "Nome do campo com erro", example = "email")
    private String field;
    
    @Schema(description = "Valor rejeitado", example = "email-invalido")
    private Object rejectedValue;
    
    @Schema(description = "Mensagem de erro", example = "Email deve ter um formato válido")
    private String message;
    
    @Schema(description = "Código do erro", example = "Email.invalid")
    private String code;

    public FieldError() {}

    public FieldError(String field, Object rejectedValue, String message) {
        this.field = field;
        this.rejectedValue = rejectedValue;
        this.message = message;
    }

    public FieldError(String field, Object rejectedValue, String message, String code) {
        this.field = field;
        this.rejectedValue = rejectedValue;
        this.message = message;
        this.code = code;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}