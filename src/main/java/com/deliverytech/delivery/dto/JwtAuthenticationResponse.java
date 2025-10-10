package com.deliverytech.delivery.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;

@Schema(description = "Resposta de autenticação com token JWT")
public class JwtAuthenticationResponse {
    
    @Schema(description = "Token JWT de acesso", example = "eyJhbGciOiJIUzUxMiJ9...")
    private String accessToken;
    
    @Schema(description = "Tipo do token", example = "Bearer")
    private String tokenType = "Bearer";
    
    @Schema(description = "Username do usuário", example = "admin")
    private String username;
    
    @Schema(description = "Roles do usuário", example = "[\"ADMIN\"]")
    private Set<String> roles;
    
    public JwtAuthenticationResponse() {}
    
    public JwtAuthenticationResponse(String accessToken, String username, Set<String> roles) {
        this.accessToken = accessToken;
        this.username = username;
        this.roles = roles;
    }
    
    public String getAccessToken() {
        return accessToken;
    }
    
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
    
    public String getTokenType() {
        return tokenType;
    }
    
    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public Set<String> getRoles() {
        return roles;
    }
    
    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}