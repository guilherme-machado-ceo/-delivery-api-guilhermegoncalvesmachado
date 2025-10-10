package com.deliverytech.delivery.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Schema(description = "DTO para cálculo de pedido sem salvar")
public class CalculoPedidoDTO {

    @NotNull(message = "ID do restaurante é obrigatório")
    @Schema(description = "ID do restaurante", example = "1", required = true)
    private Long restauranteId;

    @NotEmpty(message = "Lista de itens não pode estar vazia")
    @Valid
    @Schema(description = "Lista de itens para cálculo", required = true)
    private List<ItemPedidoDTO> itens;

    @Schema(description = "CEP para cálculo de taxa de entrega", example = "01310-100")
    private String cep;

    // Constructors
    public CalculoPedidoDTO() {}

    public CalculoPedidoDTO(Long restauranteId, List<ItemPedidoDTO> itens, String cep) {
        this.restauranteId = restauranteId;
        this.itens = itens;
        this.cep = cep;
    }

    // Getters and Setters
    public Long getRestauranteId() {
        return restauranteId;
    }

    public void setRestauranteId(Long restauranteId) {
        this.restauranteId = restauranteId;
    }

    public List<ItemPedidoDTO> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedidoDTO> itens) {
        this.itens = itens;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }
}