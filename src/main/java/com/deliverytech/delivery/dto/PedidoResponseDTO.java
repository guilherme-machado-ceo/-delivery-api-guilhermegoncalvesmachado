package com.deliverytech.delivery.dto;

import com.deliverytech.delivery.model.StatusPedido;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "DTO de resposta com pedido completo")
public class PedidoResponseDTO {

    @Schema(description = "ID único do pedido", example = "1")
    private Long id;

    @Schema(description = "Dados do cliente")
    private ClienteResponseDTO cliente;

    @Schema(description = "Dados do restaurante")
    private RestauranteResponseDTO restaurante;

    @Schema(description = "Data e hora do pedido", example = "2025-10-08T15:30:00")
    private LocalDateTime dataPedido;

    @Schema(description = "Status atual do pedido", example = "PENDENTE")
    private StatusPedido status;

    @Schema(description = "Valor total do pedido", example = "35.90")
    private BigDecimal valorTotal;

    @Schema(description = "Endereço para entrega", example = "Rua das Flores, 123 - São Paulo/SP")
    private String enderecoCoberto;

    @Schema(description = "Observações do pedido", example = "Sem cebola na pizza")
    private String observacoes;

    @Schema(description = "Lista de itens do pedido")
    private List<ItemPedidoResponseDTO> itens;

    // Constructors
    public PedidoResponseDTO() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ClienteResponseDTO getCliente() {
        return cliente;
    }

    public void setCliente(ClienteResponseDTO cliente) {
        this.cliente = cliente;
    }

    public RestauranteResponseDTO getRestaurante() {
        return restaurante;
    }

    public void setRestaurante(RestauranteResponseDTO restaurante) {
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

    public String getEnderecoCoberto() {
        return enderecoCoberto;
    }

    public void setEnderecoCoberto(String enderecoCoberto) {
        this.enderecoCoberto = enderecoCoberto;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public List<ItemPedidoResponseDTO> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedidoResponseDTO> itens) {
        this.itens = itens;
    }
}