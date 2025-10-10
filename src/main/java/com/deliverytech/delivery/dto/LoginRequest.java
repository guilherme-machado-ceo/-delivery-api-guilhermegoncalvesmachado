package com.deliverytech.delivery.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "DTO para requisição de login")
public class LoginRequest {
    
    @NotBlank(message = "Username é obrigatório")
    @Schema(description = "Nome de usuário", example = "admin", required = true)
    private String username;
    
    @NotBlank(message = "Password é obrigatório")
    @Schema(description = "Senha do usuário", example = "admin123", required = true)
    private String password;
    
    public LoginRequest() {}
    
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
}