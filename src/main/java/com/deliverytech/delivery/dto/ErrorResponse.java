package com.deliverytech.delivery.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Resposta de erro da API")
public class ErrorResponse {
    
    @Schema(description = "Indica que a operação falhou", example = "false")
    private boolean success = false;
    
    @Schema(description = "Detalhes do erro")
    private ErrorDetails error;
    
    @Schema(description = "Timestamp do erro", example = "2024-01-15T10:30:00")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;
    
    public ErrorResponse() {
        this.timestamp = LocalDateTime.now();
    }
    
    public ErrorResponse(ErrorDetails error) {
        this.error = error;
        this.timestamp = LocalDateTime.now();
    }
    
    public static ErrorResponse of(String code, String message) {
        return new ErrorResponse(new ErrorDetails(code, message));
    }
    
    public static ErrorResponse of(String code, String message, String details) {
        return new ErrorResponse(new ErrorDetails(code, message, details));
    }
    
    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public ErrorDetails getError() {
        return error;
    }
    
    public void setError(ErrorDetails error) {
        this.error = error;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    // Métodos de conveniência para compatibilidade com testes
    public String getType() {
        return error != null ? error.getCode() : null;
    }
    
    public String getDetail() {
        return error != null ? error.getDetails() : null;
    }
    
    public List<String> getErrors() {
        return error != null ? error.getFieldErrors() : null;
    }
    
    @Schema(description = "Detalhes específicos do erro")
    public static class ErrorDetails {
        
        @Schema(description = "Código do erro", example = "VALIDATION_ERROR")
        private String code;
        
        @Schema(description = "Mensagem do erro", example = "Dados inválidos")
        private String message;
        
        @Schema(description = "Detalhes adicionais", example = "Email já existe")
        private String details;
        
        @Schema(description = "Lista de erros de campo")
        private List<String> fieldErrors;
        
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
        
        public ErrorDetails(String code, String message, List<String> fieldErrors) {
            this.code = code;
            this.message = message;
            this.fieldErrors = fieldErrors;
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
        
        public List<String> getFieldErrors() {
            return fieldErrors;
        }
        
        public void setFieldErrors(List<String> fieldErrors) {
            this.fieldErrors = fieldErrors;
        }
    }
}