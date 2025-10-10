package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.JwtAuthenticationResponse;
import com.deliverytech.delivery.dto.LoginRequest;
import com.deliverytech.delivery.security.JwtTokenProvider;
import com.deliverytech.delivery.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticação", description = "Operações de autenticação e autorização")
public class AuthController {
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtTokenProvider tokenProvider;
    
    @PostMapping("/login")
    @Operation(summary = "Fazer login", description = "Autentica usuário e retorna token JWT")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
        @ApiResponse(responseCode = "401", description = "Credenciais inválidas"),
        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")
    })
    public ResponseEntity<JwtAuthenticationResponse> authenticateUser(
            @Valid @RequestBody @Parameter(description = "Credenciais de login") LoginRequest loginRequest) {
        
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        String jwt = tokenProvider.generateToken(authentication);
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        
        Set<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt, userPrincipal.getUsername(), roles));
    }
    
    @GetMapping("/me")
    @Operation(summary = "Obter informações do usuário atual", description = "Retorna informações do usuário autenticado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Informações do usuário retornadas"),
        @ApiResponse(responseCode = "401", description = "Token inválido ou expirado")
    })
    public ResponseEntity<UserInfoResponse> getCurrentUser(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        
        Set<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        
        UserInfoResponse userInfo = new UserInfoResponse(
                userPrincipal.getId(),
                userPrincipal.getUsername(),
                roles
        );
        
        return ResponseEntity.ok(userInfo);
    }
    
    // Classe interna para resposta de informações do usuário
    public static class UserInfoResponse {
        private Long id;
        private String username;
        private Set<String> roles;
        
        public UserInfoResponse(Long id, String username, Set<String> roles) {
            this.id = id;
            this.username = username;
            this.roles = roles;
        }
        
        // Getters
        public Long getId() { return id; }
        public String getUsername() { return username; }
        public Set<String> getRoles() { return roles; }
    }
}