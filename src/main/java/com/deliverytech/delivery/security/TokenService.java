package com.deliverytech.delivery.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.deliverytech.delivery.model.User;
import com.deliverytech.delivery.model.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.stream.Collectors;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    @Value("${api.security.token.expiration:86400000}")
    private long expiration; // 24 horas em milissegundos

    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String roles = user.getRoles().stream()
                             .map(Role::name)
                             .collect(Collectors.joining(","));

            return JWT.create()
                    .withIssuer("delivery-api")
                    .withSubject(user.getEmail())
                    .withClaim("userId", user.getId())
                    .withClaim("role", roles)
                    .withClaim("restauranteId", user.getRestauranteId())
                    .withExpiresAt(getExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token", exception);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("delivery-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            throw new JWTVerificationException("Token inválido ou expirado");
        }
    }

    private Instant getExpirationDate() {
        return Instant.now().plusMillis(expiration);
    }
}
