package com.deliverytech.delivery.util;

import com.deliverytech.delivery.dto.ProdutoDTO;
import com.deliverytech.delivery.dto.ProdutoResponseDTO;
import com.deliverytech.delivery.model.Produto;
import java.math.BigDecimal;

/**
 * Classe utilitária para criação de dados de teste relacionados a Produto
 */
public class ProdutoTestData {

    public static ProdutoDTO createValidProdutoDTO() {
        ProdutoDTO dto = new ProdutoDTO();
        dto.setNome("Pizza Margherita");
        dto.setDescricao("Deliciosa pizza com molho de tomate e mussarela");
        dto.setPreco(new BigDecimal("25.90"));
        dto.setCategoria("Pizza");
        dto.setRestauranteId(1L);
        dto.setDisponivel(true);
        return dto;
    }

    public static ProdutoDTO createProdutoDTOWithRestaurante(Long restauranteId) {
        ProdutoDTO dto = createValidProdutoDTO();
        dto.setRestauranteId(restauranteId);
        return dto;
    }

    public static ProdutoDTO createInvalidProdutoDTO() {
        ProdutoDTO dto = new ProdutoDTO();
        dto.setNome(""); // Nome inválido
        dto.setDescricao(""); // Descrição inválida
        dto.setPreco(new BigDecimal("-1.00")); // Preço inválido
        dto.setCategoria(""); // Categoria inválida
        dto.setRestauranteId(null); // Restaurante inválido
        return dto;
    }

    public static Produto createValidProduto() {
        Produto produto = new Produto();
        produto.setId(1L);
        produto.setNome("Pizza Margherita");
        produto.setDescricao("Deliciosa pizza com molho de tomate e mussarela");
        produto.setPreco(new BigDecimal("25.90"));
        produto.setCategoria("Pizza");
        produto.setDisponivel(true);
        return produto;
    }

    public static Produto createProdutoWithId(Long id) {
        Produto produto = createValidProduto();
        produto.setId(id);
        return produto;
    }

    public static Produto createUnavailableProduto() {
        Produto produto = createValidProduto();
        produto.setDisponivel(false);
        return produto;
    }

    public static ProdutoResponseDTO createValidProdutoResponseDTO() {
        ProdutoResponseDTO dto = new ProdutoResponseDTO();
        dto.setId(1L);
        dto.setNome("Pizza Margherita");
        dto.setDescricao("Deliciosa pizza com molho de tomate e mussarela");
        dto.setPreco(new BigDecimal("25.90"));
        dto.setCategoria("Pizza");
        dto.setDisponivel(true);
        dto.setRestaurante(createValidRestauranteResponseDTO());
        return dto;
    }

    public static ProdutoResponseDTO createProdutoResponseDTOWithId(Long id) {
        ProdutoResponseDTO dto = createValidProdutoResponseDTO();
        dto.setId(id);
        return dto;
    }

    // Builder pattern para maior flexibilidade
    public static class ProdutoDTOBuilder {
        private ProdutoDTO dto = new ProdutoDTO();

        public ProdutoDTOBuilder nome(String nome) {
            dto.setNome(nome);
            return this;
        }

        public ProdutoDTOBuilder descricao(String descricao) {
            dto.setDescricao(descricao);
            return this;
        }

        public ProdutoDTOBuilder preco(BigDecimal preco) {
            dto.setPreco(preco);
            return this;
        }

        public ProdutoDTOBuilder categoria(String categoria) {
            dto.setCategoria(categoria);
            return this;
        }

        public ProdutoDTOBuilder restauranteId(Long restauranteId) {
            dto.setRestauranteId(restauranteId);
            return this;
        }

        public ProdutoDTOBuilder disponivel(Boolean disponivel) {
            dto.setDisponivel(disponivel);
            return this;
        }

        public ProdutoDTO build() {
            return dto;
        }
    }

    public static ProdutoDTOBuilder builder() {
        return new ProdutoDTOBuilder();
    }
    
    // Método auxiliar para criar RestauranteResponseDTO
    private static com.deliverytech.delivery.dto.RestauranteResponseDTO createValidRestauranteResponseDTO() {
        com.deliverytech.delivery.dto.RestauranteResponseDTO restaurante = new com.deliverytech.delivery.dto.RestauranteResponseDTO();
        restaurante.setId(1L);
        restaurante.setNome("Pizzaria do João");
        restaurante.setCategoria("Pizzaria");
        restaurante.setEndereco("Rua das Pizzas, 456 - Centro - São Paulo/SP - CEP: 01234-567");
        restaurante.setTaxaEntrega(new java.math.BigDecimal("5.50"));
        restaurante.setAvaliacao(4.5);
        restaurante.setAtivo(true);
        return restaurante;
    }
}