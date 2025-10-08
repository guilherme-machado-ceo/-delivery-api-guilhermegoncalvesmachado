package com.deliverytech.delivery.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Entity
@Schema(description = "Entidade que representa um restaurante no sistema")
public class Restaurante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único do restaurante", example = "1")
    private Long id;
    
    @NotBlank(message = "Nome do restaurante é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    @Schema(description = "Nome do restaurante", example = "Pizzaria do João", required = true)
    private String nome;
    
    @NotBlank(message = "Categoria é obrigatória")
    @Schema(description = "Categoria do restaurante", example = "Pizzaria", required = true)
    private String categoria;
    
    @NotBlank(message = "Endereço é obrigatório")
    @Size(max = 255, message = "Endereço deve ter no máximo 255 caracteres")
    @Schema(description = "Endereço do restaurante", example = "Rua das Pizzas, 456 - São Paulo/SP")
    private String endereco;
    
    @NotNull(message = "Taxa de entrega é obrigatória")
    @DecimalMin(value = "0.0", message = "Taxa de entrega deve ser positiva")
    @Schema(description = "Taxa de entrega do restaurante", example = "5.50")
    private BigDecimal taxaEntrega;
    
    @DecimalMin(value = "0.0", message = "Avaliação deve ser positiva")
    @DecimalMax(value = "5.0", message = "Avaliação deve ser no máximo 5.0")
    @Schema(description = "Avaliação média do restaurante", example = "4.5")
    private Double avaliacao;
    
    @Schema(description = "Status ativo do restaurante", example = "true")
    private boolean ativo = true;

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

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}