package com.deliverytech.delivery.config;

import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

/**
 * Configuração específica para testes
 */
@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public ModelMapper testModelMapper() {
        return new ModelMapper();
    }

    @Bean
    @Primary
    public Clock testClock() {
        // Clock fixo para testes determinísticos
        return Clock.fixed(Instant.parse("2024-01-01T10:00:00Z"), ZoneOffset.UTC);
    }
}