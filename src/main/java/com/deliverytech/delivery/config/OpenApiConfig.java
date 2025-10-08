package com.deliverytech.delivery.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("DeliveryTech API")
                        .version("1.0.0")
                        .description("API REST para sistema de delivery desenvolvida com Spring Boot e Java 21")
                        .contact(new Contact()
                                .name("Guilherme Gonçalves Machado")
                                .email("guilherme.ceo@hubstry.com")
                                .url("https://www.linkedin.com/in/guilhermegoncalvesmachado/"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor de Desenvolvimento")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Token JWT para autenticação")))
                .tags(List.of(
                        new Tag().name("Clientes").description("Operações relacionadas aos clientes"),
                        new Tag().name("Restaurantes").description("Operações relacionadas aos restaurantes"),
                        new Tag().name("Produtos").description("Operações relacionadas aos produtos"),
                        new Tag().name("Pedidos").description("Operações relacionadas aos pedidos"),
                        new Tag().name("Autenticação").description("Operações de autenticação e autorização"),
                        new Tag().name("Sistema").description("Operações de sistema e monitoramento")
                ));
    }
}