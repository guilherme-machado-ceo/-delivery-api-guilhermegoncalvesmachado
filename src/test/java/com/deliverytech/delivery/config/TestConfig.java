package com.deliverytech.delivery.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuração específica para testes
 * Desabilita segurança e configura beans específicos para ambiente de teste
 */
@TestConfiguration
@EnableWebSecurity
@Profile("test")
public class TestConfig {

    /**
     * Configuração de segurança para testes - desabilita autenticação
     */
    @Bean
    @Primary
    public SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
            .headers(headers -> headers.frameOptions().disable()); // Para H2 Console
        
        return http.build();
    }
}