package com.deliverytech.delivery.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;

@Schema(description = "DTO para criação de pedido")
public class PedidoDTO {

    @NotNull(message = "ID do cliente é obrigatório")
    @Schema(description = "ID do cliente", example = "1", required = true)
    private Long clienteId;

    @NotNull(message = "ID do restaurante é obrigatório")
    @Schema(description = "ID do restaurante", example = "1", required = true)
    private Long restauranteId;

    @NotBlank(message = "Endereço de entrega é obrigatório")
    @Size(max = 255, message = "Endereço deve ter no máximo 255 caracteres")
    @Schema(description = "Endereço para entrega", example = "Rua das Flores, 123 - São Paulo/SP", required = true)
    private String enderecoEntrega;

    @NotBlank(message = "CEP de entrega é obrigatório")
    @Pattern(regexp = "\\d{5}-?\\d{3}", message = "CEP deve estar no formato 00000-000")
    @Schema(description = "CEP para entrega", example = "01234-567", required = true)
    private String cepEntrega;

    @Size(max = 500, message = "Observações devem ter no máximo 500 caracteres")
    @Schema(description = "Observações do pedido", example = "Entrega rápida")
    private String observacoes;

    @NotEmpty(message = "Lista de itens não pode estar vazia")
    @Valid
    @Schema(description = "Lista de itens do pedido", required = true)
    private List<ItemPedidoDTO> itens;

    // Constructors
    public PedidoDTO() {}

    public PedidoDTO(Long clienteId, Long restauranteId, String enderecoEntrega, String cepEntrega, String observacoes, List<ItemPedidoDTO> itens) {
        this.clienteId = clienteId;
        this.restauranteId = restauranteId;
        this.enderecoEntrega = enderecoEntrega;
        this.cepEntrega = cepEntrega;
        this.observacoes = observacoes;
        this.itens = itens;
    }

    // Getters and Setters
    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Long getRestauranteId() {
        return restauranteId;
    }

    public void setRestauranteId(Long restauranteId) {
        this.restauranteId = restauranteId;
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

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public List<ItemPedidoDTO> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedidoDTO> itens) {
        this.itens = itens;
    }
}