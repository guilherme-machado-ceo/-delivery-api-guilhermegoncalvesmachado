package com.deliverytech.delivery.config;

import com.deliverytech.delivery.repository.RestauranteRepository;
import com.deliverytech.delivery.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuator.health.Health;
import org.springframework.boot.actuator.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class CustomHealthIndicator implements HealthIndicator {
    
    @Autowired
    private RestauranteRepository restauranteRepository;
    
    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Override
    public Health health() {
        try {
            // Verificar se consegue acessar o banco
            long totalRestaurantes = restauranteRepository.count();
            long totalPedidos = pedidoRepository.count();
            
            // Verificar se há dados básicos
            if (totalRestaurantes == 0) {
                return Health.down()
                        .withDetail("reason", "Nenhum restaurante cadastrado")
                        .withDetail("totalRestaurantes", totalRestaurantes)
                        .withDetail("totalPedidos", totalPedidos)
                        .build();
            }
            
            return Health.up()
                    .withDetail("totalRestaurantes", totalRestaurantes)
                    .withDetail("totalPedidos", totalPedidos)
                    .withDetail("status", "Sistema operacional")
                    .build();
                    
        } catch (Exception e) {
            return Health.down()
                    .withDetail("reason", "Erro ao acessar banco de dados")
                    .withDetail("error", e.getMessage())
                    .build();
        }
    }
}