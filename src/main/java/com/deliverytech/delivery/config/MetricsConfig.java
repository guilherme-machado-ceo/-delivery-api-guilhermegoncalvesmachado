package com.deliverytech.delivery.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {
    
    @Bean
    public Counter pedidosCriadosCounter(MeterRegistry meterRegistry) {
        return Counter.builder("pedidos.criados")
                .description("Número total de pedidos criados")
                .tag("tipo", "criacao")
                .register(meterRegistry);
    }
    
    @Bean
    public Counter pedidosCanceladosCounter(MeterRegistry meterRegistry) {
        return Counter.builder("pedidos.cancelados")
                .description("Número total de pedidos cancelados")
                .tag("tipo", "cancelamento")
                .register(meterRegistry);
    }
    
    @Bean
    public Timer pedidoProcessamentoTimer(MeterRegistry meterRegistry) {
        return Timer.builder("pedidos.processamento.tempo")
                .description("Tempo de processamento de pedidos")
                .register(meterRegistry);
    }
    
    @Bean
    public Counter restaurantesAcessadosCounter(MeterRegistry meterRegistry) {
        return Counter.builder("restaurantes.acessos")
                .description("Número de acessos aos restaurantes")
                .register(meterRegistry);
    }
    
    @Bean
    public Counter produtosBuscadosCounter(MeterRegistry meterRegistry) {
        return Counter.builder("produtos.buscas")
                .description("Número de buscas de produtos")
                .register(meterRegistry);
    }
}