package com.deliverytech.delivery.dto;

import com.deliverytech.delivery.model.StatusPedido;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "DTO de resumo do pedido para listagens")
public class PedidoResumoDTO {

    @Schema(description = "ID único do pedido", example = "1")
    private Long id;

    @Schema(description = "Nome do cliente", example = "João Silva")
    private String nomeCliente;

    @Schema(description = "Nome do restaurante", example = "Pizzaria do João")
    private String nomeRestaurante;

    @Schema(description = "Data e hora do pedido", example = "2025-10-08T15:30:00")
    private LocalDateTime dataPedido;

    @Schema(description = "Status atual do pedido", example = "PENDENTE")
    private StatusPedido status;

    @Schema(description = "Valor total do pedido", example = "35.90")
    private BigDecimal valorTotal;

    @Schema(description = "Quantidade de itens", example = "3")
    private Integer quantidadeItens;

    // Constructors
    public PedidoResumoDTO() {}

    public PedidoResumoDTO(Long id, String nomeCliente, String nomeRestaurante, 
                          LocalDateTime dataPedido, StatusPedido status, 
                          BigDecimal valorTotal, Integer quantidadeItens) {
        this.id = id;
        this.nomeCliente = nomeCliente;
        this.nomeRestaurante = nomeRestaurante;
        this.dataPedido = dataPedido;
        this.status = status;
        this.valorTotal = valorTotal;
        this.quantidadeItens = quantidadeItens;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getNomeRestaurante() {
        return nomeRestaurante;
    }

    public void setNomeRestaurante(String nomeRestaurante) {
        this.nomeRestaurante = nomeRestaurante;
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

    public Integer getQuantidadeItens() {
        return quantidadeItens;
    }

    public void setQuantidadeItens(Integer quantidadeItens) {
        this.quantidadeItens = quantidadeItens;
    }
}