package com.deliverytech.delivery.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Schema(description = "DTO para criação e atualização de produto")
public class ProdutoDTO {

    @NotBlank(message = "Nome do produto é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    @Schema(description = "Nome do produto", example = "Pizza Margherita", required = true)
    private String nome;

    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    @Schema(description = "Descrição detalhada do produto", example = "Pizza com molho de tomate, mussarela e manjericão")
    private String descricao;

    @NotNull(message = "Preço é obrigatório")
    @DecimalMin(value = "0.01", message = "Preço deve ser maior que zero")
    @Schema(description = "Preço do produto", example = "25.90", required = true)
    private BigDecimal preco;

    @NotBlank(message = "Categoria é obrigatória")
    @Schema(description = "Categoria do produto", example = "Pizza", required = true)
    private String categoria;

    @NotNull(message = "Restaurante é obrigatório")
    @Schema(description = "ID do restaurante", example = "1", required = true)
    private Long restauranteId;

    @Schema(description = "Disponibilidade do produto", example = "true")
    private Boolean disponivel = true;

    // Constructors
    public ProdutoDTO() {}

    public ProdutoDTO(String nome, String descricao, BigDecimal preco, String categoria, Long restauranteId) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.categoria = categoria;
        this.restauranteId = restauranteId;
    }

    // Getters and Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Long getRestauranteId() {
        return restauranteId;
    }

    public void setRestauranteId(Long restauranteId) {
        this.restauranteId = restauranteId;
    }

    public Boolean getDisponivel() {
        return disponivel;
    }

    public void setDisponivel(Boolean disponivel) {
        this.disponivel = disponivel;
    }
}