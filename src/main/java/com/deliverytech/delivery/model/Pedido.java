package com.deliverytech.delivery.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Schema(description = "Entidade que representa um pedido no sistema")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único do pedido", example = "1")
    private Long id;
    
    @ManyToOne
    @NotNull(message = "Cliente é obrigatório")
    @Schema(description = "Cliente que fez o pedido", required = true)
    private Cliente cliente;
    
    @ManyToOne
    @NotNull(message = "Restaurante é obrigatório")
    @Schema(description = "Restaurante do pedido", required = true)
    private Restaurante restaurante;
    
    @Schema(description = "Data e hora do pedido", example = "2025-10-08T15:30:00")
    private LocalDateTime dataPedido = LocalDateTime.now();
    
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Status é obrigatório")
    @Schema(description = "Status atual do pedido", example = "PENDENTE", required = true)
    private StatusPedido status;
    
    @NotNull(message = "Valor total é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor total deve ser maior que zero")
    @Schema(description = "Valor total do pedido", example = "35.90", required = true)
    private BigDecimal valorTotal;
    
    @NotBlank(message = "Endereço de entrega é obrigatório")
    @Size(max = 255, message = "Endereço deve ter no máximo 255 caracteres")
    @Schema(description = "Endereço para entrega", example = "Rua das Flores, 123 - São Paulo/SP", required = true)
    private String enderecoEntrega;
    
    @NotBlank(message = "CEP de entrega é obrigatório")
    @Schema(description = "CEP para entrega", example = "01234-567", required = true)
    private String cepEntrega;
    
    @DecimalMin(value = "0.00", message = "Taxa de entrega não pode ser negativa")
    @Schema(description = "Taxa de entrega", example = "5.90")
    private BigDecimal taxaEntrega;
    
    @DecimalMin(value = "0.01", message = "Total deve ser maior que zero")
    @Schema(description = "Total do pedido (itens + taxa)", example = "35.90")
    private BigDecimal total;
    
    @Size(max = 500, message = "Observações devem ter no máximo 500 caracteres")
    @Schema(description = "Observações adicionais do pedido", example = "Sem cebola na pizza")
    private String observacoes;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Restaurante getRestaurante() {
        return restaurante;
    }

    public void setRestaurante(Restaurante restaurante) {
        this.restaurante = restaurante;
    }

    public LocalDateTime getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(LocalDateTime dataPedido) {
        this.dataPedido = dataPedido;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getEnderecoEntrega() {
        return enderecoEntrega;
    }

    public void setEnderecoEntrega(String enderecoEntrega) {
        this.enderecoEntrega = enderecoEntrega;
    }

    public String getCepEntrega() {
        return cepEntrega;
    }

    public void setCepEntrega(String cepEntrega) {
        this.cepEntrega = cepEntrega;
    }

    public BigDecimal getTaxaEntrega() {
        return taxaEntrega;
    }

    public void setTaxaEntrega(BigDecimal taxaEntrega) {
        this.taxaEntrega = taxaEntrega;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}