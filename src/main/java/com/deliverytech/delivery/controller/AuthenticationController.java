package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.LoginRequest;
import com.deliverytech.delivery.dto.LoginResponse;
import com.deliverytech.delivery.dto.RegisterRequest;
import com.deliverytech.delivery.dto.UserResponse;
import com.deliverytech.delivery.model.User;
import com.deliverytech.delivery.repository.UserRepository;
import com.deliverytech.delivery.security.TokenService;
import com.deliverytech.delivery.util.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    @Value("${api.security.token.expiration:86400000}")
    private long tokenExpiration;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password())
        );
        User user = (User) authentication.getPrincipal();
        String token = tokenService.generateToken(user);
        UserResponse userResponse = UserResponse.from(user);
        return ResponseEntity.ok(new LoginResponse(token, tokenExpiration, userResponse));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.email())) {
            return ResponseEntity.badRequest().body("Erro: Email já está em uso!");
        }

        User newUser = new User(
                registerRequest.nome(),
                registerRequest.email(),
                passwordEncoder.encode(registerRequest.senha()),
                registerRequest.roles(),
                registerRequest.restauranteId()
        );

        userRepository.save(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.from(newUser));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser() {
        User currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(UserResponse.from(currentUser));
    }
}
