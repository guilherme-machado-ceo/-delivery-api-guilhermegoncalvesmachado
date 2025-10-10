package com.deliverytech.delivery.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "DTO de resposta com dados do produto")
public class ProdutoResponseDTO {

    @Schema(description = "ID único do produto", example = "1")
    private Long id;

    @Schema(description = "Nome do produto", example = "Pizza Margherita")
    private String nome;

    @Schema(description = "Descrição do produto", example = "Pizza com molho de tomate, mussarela e manjericão")
    private String descricao;

    @Schema(description = "Preço do produto", example = "25.90")
    private BigDecimal preco;

    @Schema(description = "Categoria do produto", example = "Pizza")
    private String categoria;

    @Schema(description = "Disponibilidade do produto", example = "true")
    private Boolean disponivel;

    @Schema(description = "Dados do restaurante")
    private RestauranteResponseDTO restaurante;

    // Constructors
    public ProdutoResponseDTO() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Boolean getDisponivel() {
        return disponivel;
    }

    public void setDisponivel(Boolean disponivel) {
        this.disponivel = disponivel;
    }

    public RestauranteResponseDTO getRestaurante() {
        return restaurante;
    }

    public void setRestaurante(RestauranteResponseDTO restaurante) {
        this.restaurante = restaurante;
    }
    
    // Método auxiliar para obter ID do restaurante
    public Long getRestauranteId() {
        return restaurante != null ? restaurante.getId() : null;
    }
}