package com.deliverytech.delivery.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class ItemPedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    private Pedido pedido;
    
    @ManyToOne
    private Produto produto;
    
    private Integer quantidade;
    private BigDecimal precoUnitario;
    private BigDecimal precoTotal;
    private String observacao;

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
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

    // Compatibility methods expected by services: subtotal aliases
    public BigDecimal getSubtotal() {
        return this.precoTotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.precoTotal = subtotal;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    // Método para calcular o preço total
    @PrePersist
    @PreUpdate
    private void calcularPrecoTotal() {
        if (this.precoUnitario != null && this.quantidade != null) {
            this.precoTotal = this.precoUnitario.multiply(BigDecimal.valueOf(this.quantidade));
        }
    }
}