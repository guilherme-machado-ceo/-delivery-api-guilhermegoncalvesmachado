package com.deliverytech.delivery.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

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
}