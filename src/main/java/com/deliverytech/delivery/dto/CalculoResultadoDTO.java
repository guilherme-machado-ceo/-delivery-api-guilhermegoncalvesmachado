package com.deliverytech.delivery.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.List;

@Schema(description = "DTO com resultado do cálculo do pedido")
public class CalculoResultadoDTO {

    @Schema(description = "Subtotal dos itens", example = "45.00")
    private BigDecimal subtotal;

    @Schema(description = "Taxa de entrega", example = "5.50")
    private BigDecimal taxaEntrega;

    @Schema(description = "Valor total do pedido", example = "50.50")
    private BigDecimal valorTotal;

    @Schema(description = "Detalhes dos itens calculados")
    private List<ItemCalculadoDTO> itensCalculados;

    // Constructors
    public CalculoResultadoDTO() {}

    public CalculoResultadoDTO(BigDecimal subtotal, BigDecimal taxaEntrega, BigDecimal valorTotal, List<ItemCalculadoDTO> itensCalculados) {
        this.subtotal = subtotal;
        this.taxaEntrega = taxaEntrega;
        this.valorTotal = valorTotal;
        this.itensCalculados = itensCalculados;
    }

    // Getters and Setters
    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getTaxaEntrega() {
        return taxaEntrega;
    }

    public void setTaxaEntrega(BigDecimal taxaEntrega) {
        this.taxaEntrega = taxaEntrega;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public List<ItemCalculadoDTO> getItensCalculados() {
        return itensCalculados;
    }

    public void setItensCalculados(List<ItemCalculadoDTO> itensCalculados) {
        this.itensCalculados = itensCalculados;
    }

    // Métodos auxiliares para compatibilidade
    public BigDecimal getTotalItens() {
        return subtotal;
    }

    public void setTotalItens(BigDecimal totalItens) {
        this.subtotal = totalItens;
    }

    public BigDecimal getTotalPedido() {
        return valorTotal;
    }

    public void setTotalPedido(BigDecimal totalPedido) {
        this.valorTotal = totalPedido;
    }

    @Schema(description = "DTO com item calculado")
    public static class ItemCalculadoDTO {
        @Schema(description = "Nome do produto", example = "Pizza Margherita")
        private String nomeProduto;
        
        @Schema(description = "Quantidade", example = "2")
        private Integer quantidade;
        
        @Schema(description = "Preço unitário", example = "25.90")
        private BigDecimal precoUnitario;
        
        @Schema(description = "Total do item", example = "51.80")
        private BigDecimal totalItem;

        public ItemCalculadoDTO() {}

        public ItemCalculadoDTO(String nomeProduto, Integer quantidade, BigDecimal precoUnitario, BigDecimal totalItem) {
            this.nomeProduto = nomeProduto;
            this.quantidade = quantidade;
            this.precoUnitario = precoUnitario;
            this.totalItem = totalItem;
        }

        // Getters and Setters
        public String getNomeProduto() {
            return nomeProduto;
        }

        public void setNomeProduto(String nomeProduto) {
            this.nomeProduto = nomeProduto;
        }

        public Integer getQuantidade() {
            return quantidade;
        }

        public void setQuantidade(Integer quantidade) {
            this.quantidade = quantidade;
        }

        public BigDecimal getPrecoUnitario() {
            return precoUnitario;
        }

        public void setPrecoUnitario(BigDecimal precoUnitario) {
            this.precoUnitario = precoUnitario;
        }

        public BigDecimal getTotalItem() {
            return totalItem;
        }

        public void setTotalItem(BigDecimal totalItem) {
            this.totalItem = totalItem;
        }
    }
}