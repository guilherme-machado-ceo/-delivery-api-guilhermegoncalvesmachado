package com.deliverytech.delivery.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Entity
@Schema(description = "Entidade que representa um produto de um restaurante")
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único do produto", example = "1")
    private Long id;
    
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
    
    @Schema(description = "Disponibilidade do produto", example = "true")
    private boolean disponivel = true;
    
    @ManyToOne
    @NotNull(message = "Restaurante é obrigatório")
    @Schema(description = "Restaurante ao qual o produto pertence")
    private Restaurante restaurante;

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

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    public Restaurante getRestaurante() {
        return restaurante;
    }

    public void setRestaurante(Restaurante restaurante) {
        this.restaurante = restaurante;
    }
}