package com.deliverytech.delivery.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Resposta padrão para erros da API")
public class ErrorResponse {
    
    @Schema(description = "Timestamp do erro", example = "2025-10-08T15:30:00")
    private LocalDateTime timestamp;
    
    @Schema(description = "Código de status HTTP", example = "400")
    private int status;
    
    @Schema(description = "Tipo do erro", example = "Bad Request")
    private String error;
    
    @Schema(description = "Mensagem do erro", example = "Dados de entrada inválidos")
    private String message;
    
    @Schema(description = "Caminho da requisição", example = "/api/clientes")
    private String path;
    
    @Schema(description = "Lista de erros de campo específicos")
    private List<FieldError> fieldErrors;

    public ErrorResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponse(int status, String error, String message, String path) {
        this();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    // Getters and Setters
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<FieldError> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(List<FieldError> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }
}