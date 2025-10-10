package com.deliverytech.delivery.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "DTO de resposta para item do pedido")
public class ItemPedidoResponseDTO {

    @Schema(description = "ID único do item", example = "1")
    private Long id;

    @Schema(description = "Dados do produto")
    private ProdutoResponseDTO produto;

    @Schema(description = "Quantidade do produto", example = "2")
    private Integer quantidade;

    @Schema(description = "Preço unitário no momento do pedido", example = "25.90")
    private BigDecimal precoUnitario;

    @Schema(description = "Preço total do item", example = "51.80")
    private BigDecimal precoTotal;

    @Schema(description = "Observações do item", example = "Sem cebola")
    private String observacao;

    // Constructors
    public ItemPedidoResponseDTO() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProdutoResponseDTO getProduto() {
        return produto;
    }

    public void setProduto(ProdutoResponseDTO produto) {
        this.produto = produto;
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

    public BigDecimal getPrecoTotal() {
        return precoTotal;
    }

    public void setPrecoTotal(BigDecimal precoTotal) {
        this.precoTotal = precoTotal;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}