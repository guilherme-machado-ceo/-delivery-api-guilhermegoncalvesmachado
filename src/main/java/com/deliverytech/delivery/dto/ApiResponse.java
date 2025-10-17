package com.deliverytech.delivery.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

@Schema(description = "Resposta padrão da API")
public class ApiResponse<T> {
    
    @Schema(description = "Indica se a operação foi bem-sucedida", example = "true")
    private boolean success;
    
    @Schema(description = "Dados da resposta")
    private T data;
    
    @Schema(description = "Mensagem descritiva da operação", example = "Operação realizada com sucesso")
    private String message;
    
    @Schema(description = "Timestamp da resposta", example = "2024-01-15T10:30:00")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;
    
    public ApiResponse() {
        this.timestamp = LocalDateTime.now();
    }
    
    public ApiResponse(boolean success, T data, String message) {
        this.success = success;
        this.data = data;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
    
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, "Operação realizada com sucesso");
    }
    
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(true, data, message);
    }
    
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, null, message);
    }
    
    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public T getData() {
        return data;
    }
    
    public void setData(T data) {
        this.data = data;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}