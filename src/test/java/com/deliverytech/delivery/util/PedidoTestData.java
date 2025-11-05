package com.deliverytech.delivery.util;

import com.deliverytech.delivery.dto.*;
import com.deliverytech.delivery.model.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Classe utilitária para criação de dados de teste relacionados a Pedido
 */
public class PedidoTestData {

    public static PedidoDTO createValidPedidoDTO() {
        PedidoDTO dto = new PedidoDTO();
        dto.setClienteId(1L);
        dto.setRestauranteId(1L);
        dto.setEnderecoEntrega("Rua das Flores, 123 - São Paulo/SP");
        dto.setCepEntrega("01234-567");
        dto.setObservacoes("Sem cebola na pizza");
        dto.setItens(Arrays.asList(createValidItemPedidoDTO()));
        dto.setFormaPagamento("PIX");
        return dto;
    }

    public static ItemPedidoDTO createValidItemPedidoDTO() {
        ItemPedidoDTO item = new ItemPedidoDTO();
        item.setProdutoId(1L);
        item.setQuantidade(2);
        item.setObservacao("Sem cebola");
        return item;
    }

    public static ItemPedidoDTO createItemPedidoDTOWithProduct(Long produtoId, Integer quantidade) {
        ItemPedidoDTO item = new ItemPedidoDTO();
        item.setProdutoId(produtoId);
        item.setQuantidade(quantidade);
        return item;
    }

    public static PedidoDTO createPedidoDTOWithItems(List<ItemPedidoDTO> itens) {
        PedidoDTO dto = createValidPedidoDTO();
        dto.setItens(itens);
        return dto;
    }

    public static PedidoDTO createPedidoDTOWithCliente(Long clienteId) {
        PedidoDTO dto = createValidPedidoDTO();
        dto.setClienteId(clienteId);
        return dto;
    }

    public static PedidoDTO createPedidoDTOWithRestaurante(Long restauranteId) {
        PedidoDTO dto = createValidPedidoDTO();
        dto.setRestauranteId(restauranteId);
        return dto;
    }

    public static Pedido createValidPedido() {
        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setCliente(ClienteTestData.createValidCliente());
        pedido.setRestaurante(createValidRestaurante());
        pedido.setEnderecoEntrega("Rua das Flores, 123 - São Paulo/SP");
        pedido.setCepEntrega("01234567");
        pedido.setStatus(StatusPedido.PENDENTE);
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setTaxaEntrega(new BigDecimal("5.00"));
        pedido.setTotal(new BigDecimal("30.90"));
        return pedido;
    }

    public static Pedido createPedidoWithId(Long id) {
        Pedido pedido = createValidPedido();
        pedido.setId(id);
        return pedido;
    }

    public static Pedido createPedidoWithStatus(StatusPedido status) {
        Pedido pedido = createValidPedido();
        pedido.setStatus(status);
        return pedido;
    }

    public static PedidoResponseDTO createValidPedidoResponseDTO() {
        PedidoResponseDTO dto = new PedidoResponseDTO();
        dto.setId(1L);
        dto.setCliente(ClienteTestData.createValidClienteResponseDTO());
        dto.setRestaurante(createValidRestauranteResponseDTO());
        dto.setEnderecoCoberto("Rua das Flores, 123 - São Paulo/SP");
        dto.setStatus(StatusPedido.PENDENTE);
        dto.setDataPedido(LocalDateTime.now());
        dto.setValorTotal(new BigDecimal("30.90"));
        dto.setItens(Arrays.asList(createValidItemPedidoResponseDTO()));
        return dto;
    }

    public static ItemPedidoResponseDTO createValidItemPedidoResponseDTO() {
        ItemPedidoResponseDTO item = new ItemPedidoResponseDTO();
        item.setId(1L);
        item.setProduto(createValidProdutoResponseDTO());
        item.setQuantidade(2);
        item.setPrecoUnitario(new BigDecimal("12.95"));
        item.setPrecoTotal(new BigDecimal("25.90"));
        item.setObservacao("Sem cebola");
        return item;
    }

    public static ProdutoResponseDTO createValidProdutoResponseDTO() {
        ProdutoResponseDTO produto = new ProdutoResponseDTO();
        produto.setId(1L);
        produto.setNome("Pizza Margherita");
        produto.setDescricao("Deliciosa pizza com molho de tomate e mussarela");
        produto.setPreco(new BigDecimal("12.95"));
        produto.setCategoria("Pizza");
        produto.setRestaurante(createValidRestauranteResponseDTO());
        produto.setDisponivel(true);
        return produto;
    }

    public static ProdutoResponseDTO createProdutoResponseDTOWithId(Long id) {
        ProdutoResponseDTO produto = createValidProdutoResponseDTO();
        produto.setId(id);
        return produto;
    }

    public static ProdutoResponseDTO createProdutoResponseDTOWithRestaurante(Long restauranteId) {
        ProdutoResponseDTO produto = createValidProdutoResponseDTO();
        RestauranteResponseDTO restaurante = createValidRestauranteResponseDTO();
        restaurante.setId(restauranteId);
        produto.setRestaurante(restaurante);
        return produto;
    }

    public static ProdutoResponseDTO createUnavailableProdutoResponseDTO() {
        ProdutoResponseDTO produto = createValidProdutoResponseDTO();
        produto.setDisponivel(false);
        return produto;
    }

    public static Restaurante createValidRestaurante() {
        Restaurante restaurante = new Restaurante();
        restaurante.setId(1L);
        restaurante.setNome("Pizzaria do João");
        restaurante.setCategoria("Pizzaria");
        restaurante.setEndereco("Rua A, 100 - Centro - São Paulo/SP");
        restaurante.setTaxaEntrega(new BigDecimal("5.00"));
        restaurante.setAvaliacao(4.5);
        restaurante.setAtivo(true);
        return restaurante;
    }

    public static RestauranteResponseDTO createValidRestauranteResponseDTO() {
        RestauranteResponseDTO restaurante = new RestauranteResponseDTO();
        restaurante.setId(1L);
        restaurante.setNome("Pizzaria do João");
        restaurante.setCategoria("Pizzaria");
        restaurante.setEndereco("Rua A, 100 - Centro - São Paulo/SP - CEP: 01234-567");
        restaurante.setTaxaEntrega(new BigDecimal("5.00"));
        restaurante.setAvaliacao(4.5);
        restaurante.setAtivo(true);
        return restaurante;
    }

    public static RestauranteResponseDTO createInactiveRestauranteResponseDTO() {
        RestauranteResponseDTO restaurante = createValidRestauranteResponseDTO();
        restaurante.setAtivo(false);
        return restaurante;
    }

    public static PedidoResumoDTO createValidPedidoResumoDTO() {
        PedidoResumoDTO resumo = new PedidoResumoDTO();
        resumo.setId(1L);
        resumo.setNomeCliente("João Silva");
        resumo.setNomeRestaurante("Pizzaria do João");
        resumo.setStatus(StatusPedido.PENDENTE);
        resumo.setDataPedido(LocalDateTime.now());
        resumo.setValorTotal(new BigDecimal("30.90"));
        resumo.setQuantidadeItens(2);
        return resumo;
    }

    // Dados para cenários específicos de teste
    public static PedidoDTO createInvalidPedidoDTO() {
        PedidoDTO dto = new PedidoDTO();
        dto.setClienteId(null); // Inválido
        dto.setRestauranteId(null); // Inválido
        dto.setEnderecoEntrega(""); // Inválido
        dto.setCepEntrega("123"); // Inválido
        dto.setItens(Arrays.asList()); // Lista vazia - inválido
        return dto;
    }

    public static List<ItemPedidoDTO> createMultipleItemsPedidoDTO() {
        return Arrays.asList(
            createItemPedidoDTOWithProduct(1L, 2),
            createItemPedidoDTOWithProduct(2L, 1),
            createItemPedidoDTOWithProduct(3L, 3)
        );
    }

    // Builder pattern para maior flexibilidade
    public static class PedidoDTOBuilder {
        private PedidoDTO dto = new PedidoDTO();

        public PedidoDTOBuilder clienteId(Long clienteId) {
            dto.setClienteId(clienteId);
            return this;
        }

        public PedidoDTOBuilder restauranteId(Long restauranteId) {
            dto.setRestauranteId(restauranteId);
            return this;
        }

        public PedidoDTOBuilder enderecoEntrega(String endereco) {
            dto.setEnderecoEntrega(endereco);
            return this;
        }

        public PedidoDTOBuilder cepEntrega(String cep) {
            dto.setCepEntrega(cep);
            return this;
        }

        public PedidoDTOBuilder itens(List<ItemPedidoDTO> itens) {
            dto.setItens(itens);
            return this;
        }

        public PedidoDTOBuilder observacoes(String observacoes) {
            dto.setObservacoes(observacoes);
            return this;
        }

        public PedidoDTOBuilder formaPagamento(String formaPagamento) {
            dto.setFormaPagamento(formaPagamento);
            return this;
        }

        public PedidoDTO build() {
            return dto;
        }
    }

    public static PedidoDTOBuilder builder() {
        return new PedidoDTOBuilder();
    }

    // Métodos para criar cenários específicos de erro
    public static PedidoDTO createPedidoWithInexistentProduct() {
        PedidoDTO dto = createValidPedidoDTO();
        dto.setItens(Arrays.asList(createItemPedidoDTOWithProduct(999L, 1)));
        return dto;
    }

    public static PedidoDTO createPedidoWithInsufficientStock() {
        PedidoDTO dto = createValidPedidoDTO();
        dto.setItens(Arrays.asList(createItemPedidoDTOWithProduct(1L, 100))); // Quantidade muito alta
        return dto;
    }

    public static PedidoDTO createPedidoWithInactiveCliente() {
        PedidoDTO dto = createValidPedidoDTO();
        dto.setClienteId(999L); // Cliente inativo
        return dto;
    }

    public static PedidoDTO createPedidoWithInactiveRestaurante() {
        PedidoDTO dto = createValidPedidoDTO();
        dto.setRestauranteId(999L); // Restaurante inativo
        return dto;
    }
}