package com.deliverytech.delivery.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "DTO para item do pedido")
public class ItemPedidoDTO {

    @NotNull(message = "ID do produto é obrigatório")
    @Schema(description = "ID do produto", example = "1", required = true)
    private Long produtoId;

    @NotNull(message = "Quantidade é obrigatória")
    @Min(value = 1, message = "Quantidade deve ser pelo menos 1")
    @Schema(description = "Quantidade do produto", example = "2", required = true)
    private Integer quantidade;

    @Size(max = 255, message = "Observação deve ter no máximo 255 caracteres")
    @Schema(description = "Observações do item", example = "Sem cebola")
    private String observacao;

    // Constructors
    public ItemPedidoDTO() {}

    public ItemPedidoDTO(Long produtoId, Integer quantidade, String observacao) {
        this.produtoId = produtoId;
        this.quantidade = quantidade;
        this.observacao = observacao;
    }

    // Getters and Setters
    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}