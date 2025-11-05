package com.deliverytech.delivery.util;

import com.deliverytech.delivery.dto.RestauranteDTO;
import com.deliverytech.delivery.dto.RestauranteResponseDTO;
import com.deliverytech.delivery.model.Restaurante;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Classe utilitária para criação de dados de teste relacionados a Restaurante
 */
public class RestauranteTestData {

    public static RestauranteDTO createValidRestauranteDTO() {
        RestauranteDTO dto = new RestauranteDTO();
        dto.setNome("Pizzaria do João");
        dto.setCategoria("Pizzaria");
        dto.setEndereco("Rua das Pizzas, 456 - Centro - São Paulo/SP - CEP: 01234-567");
        dto.setTaxaEntrega(new BigDecimal("5.50"));
        return dto;
    }

    public static RestauranteDTO createRestauranteDTOWithCategoria(String categoria) {
        RestauranteDTO dto = createValidRestauranteDTO();
        dto.setCategoria(categoria);
        return dto;
    }

    public static RestauranteDTO createInvalidRestauranteDTO() {
        RestauranteDTO dto = new RestauranteDTO();
        dto.setNome(""); // Nome inválido
        dto.setCategoria(""); // Categoria inválida
        dto.setEndereco(""); // Endereço inválido
        dto.setTaxaEntrega(new BigDecimal("-1.00")); // Taxa inválida
        return dto;
    }

    public static Restaurante createValidRestaurante() {
        Restaurante restaurante = new Restaurante();
        restaurante.setId(1L);
        restaurante.setNome("Pizzaria do João");
        restaurante.setCategoria("Pizzaria");
        restaurante.setEndereco("Rua das Pizzas, 456 - Centro - São Paulo/SP");
        restaurante.setTaxaEntrega(new BigDecimal("5.50"));
        restaurante.setAvaliacao(4.5);
        restaurante.setAtivo(true);
        return restaurante;
    }

    public static Restaurante createRestauranteWithId(Long id) {
        Restaurante restaurante = createValidRestaurante();
        restaurante.setId(id);
        return restaurante;
    }

    public static Restaurante createInactiveRestaurante() {
        Restaurante restaurante = createValidRestaurante();
        restaurante.setAtivo(false);
        return restaurante;
    }

    public static RestauranteResponseDTO createValidRestauranteResponseDTO() {
        RestauranteResponseDTO dto = new RestauranteResponseDTO();
        dto.setId(1L);
        dto.setNome("Pizzaria do João");
        dto.setCategoria("Pizzaria");
        dto.setEndereco("Rua das Pizzas, 456 - Centro - São Paulo/SP - CEP: 01234-567");
        dto.setTaxaEntrega(new BigDecimal("5.50"));
        dto.setAvaliacao(4.5);
        dto.setAtivo(true);
        dto.setCriadoEm(LocalDateTime.now());
        dto.setAtualizadoEm(LocalDateTime.now());
        return dto;
    }

    public static RestauranteResponseDTO createRestauranteResponseDTOWithId(Long id) {
        RestauranteResponseDTO dto = createValidRestauranteResponseDTO();
        dto.setId(id);
        return dto;
    }

    public static RestauranteResponseDTO createInactiveRestauranteResponseDTO() {
        RestauranteResponseDTO dto = createValidRestauranteResponseDTO();
        dto.setAtivo(false);
        return dto;
    }

    // Dados para cenários específicos de teste
    public static RestauranteDTO createRestauranteDTOForUpdate() {
        RestauranteDTO dto = new RestauranteDTO();
        dto.setNome("Pizzaria do João Atualizada");
        dto.setCategoria("Italiana");
        dto.setEndereco("Rua Nova, 789 - Centro - São Paulo/SP - CEP: 01234-567");
        dto.setTaxaEntrega(new BigDecimal("6.00"));
        return dto;
    }

    // Builder pattern para maior flexibilidade
    public static class RestauranteDTOBuilder {
        private RestauranteDTO dto = new RestauranteDTO();

        public RestauranteDTOBuilder nome(String nome) {
            dto.setNome(nome);
            return this;
        }

        public RestauranteDTOBuilder categoria(String categoria) {
            dto.setCategoria(categoria);
            return this;
        }

        public RestauranteDTOBuilder endereco(String endereco) {
            dto.setEndereco(endereco);
            return this;
        }

        public RestauranteDTOBuilder taxaEntrega(BigDecimal taxaEntrega) {
            dto.setTaxaEntrega(taxaEntrega);
            return this;
        }

        public RestauranteDTO build() {
            return dto;
        }
    }

    public static RestauranteDTOBuilder builder() {
        return new RestauranteDTOBuilder();
    }
}