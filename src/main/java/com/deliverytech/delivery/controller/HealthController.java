package com.deliverytech.delivery.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@Tag(name = "Sistema", description = "Operações de sistema e monitoramento")
public class HealthController {

    @GetMapping("/health")
    @Operation(summary = "Verificar saúde da aplicação", description = "Retorna o status de saúde da aplicação")
    @ApiResponse(responseCode = "200", description = "Status da aplicação retornado com sucesso")
    public Map<String, String> health() {
        return Map.of(
            "status", "UP",
            "timestamp", LocalDateTime.now().toString(),
            "service", "Delivery API",
            "javaVersion", System.getProperty("java.version")
        );
    }

    @GetMapping("/info")
    @Operation(summary = "Informações da aplicação", description = "Retorna informações detalhadas sobre a aplicação")
    @ApiResponse(responseCode = "200", description = "Informações da aplicação retornadas com sucesso")
    public AppInfo info() {
        return new AppInfo(
            "Delivery Tech API",
            "1.0.0",
            "Guilherme Gonçalves Machado",
            "JDK 21",
            "Spring Boot 3.2.x"
        );
    }

    // Record para demonstrar recurso do Java 14+ (disponível no JDK 21)
    public record AppInfo(
        String application,
        String version,
        String developer,
        String javaVersion,
        String framework
    ) {}
}