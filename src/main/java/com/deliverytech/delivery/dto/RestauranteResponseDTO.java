package com.deliverytech.delivery.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "DTO de resposta com dados completos do restaurante")
public class RestauranteResponseDTO {

    @Schema(description = "ID único do restaurante", example = "1")
    private Long id;

    @Schema(description = "Nome do restaurante", example = "Pizzaria do João")
    private String nome;

    @Schema(description = "Categoria do restaurante", example = "Pizzaria")
    private String categoria;

    @Schema(description = "Endereço do restaurante", example = "Rua das Pizzas, 456 - São Paulo/SP")
    private String endereco;

    @Schema(description = "Taxa de entrega", example = "5.50")
    private BigDecimal taxaEntrega;

    @Schema(description = "Avaliação média", example = "4.5")
    private Double avaliacao;

    @Schema(description = "Status ativo", example = "true")
    private Boolean ativo;

    // Constructors
    public RestauranteResponseDTO() {}

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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public BigDecimal getTaxaEntrega() {
        return taxaEntrega;
    }

    public void setTaxaEntrega(BigDecimal taxaEntrega) {
        this.taxaEntrega = taxaEntrega;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}