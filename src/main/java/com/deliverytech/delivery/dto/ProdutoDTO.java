package com.deliverytech.delivery.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Schema(description = "DTO para criação e atualização de produto")
public class ProdutoDTO {

    @NotBlank(message = "Nome do produto é obrigatório")
    @Size(min = 2, max = 50, message = "Nome deve ter entre 2 e 50 caracteres")
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ0-9\\s\\-&]+$", message = "Nome deve conter apenas letras, números, espaços, hífens e &")
    @Schema(description = "Nome do produto", example = "Pizza Margherita", required = true)
    private String nome;

    @NotBlank(message = "Descrição é obrigatória")
    @Size(min = 10, max = 500, message = "Descrição deve ter entre 10 e 500 caracteres")
    @Schema(description = "Descrição detalhada do produto", 
            example = "Deliciosa pizza com molho de tomate artesanal, mussarela de búfala e manjericão fresco", 
            required = true)
    private String descricao;

    @NotNull(message = "Preço é obrigatório")
    @DecimalMin(value = "0.01", message = "Preço deve ser maior que zero")
    @DecimalMax(value = "500.00", message = "Preço deve ser menor ou igual a R$ 500,00")
    @Digits(integer = 3, fraction = 2, message = "Preço deve ter no máximo 3 dígitos inteiros e 2 decimais")
    @Schema(description = "Preço do produto em reais", example = "25.90", required = true)
    private BigDecimal preco;

    @NotBlank(message = "Categoria é obrigatória")
    @Size(min = 2, max = 30, message = "Categoria deve ter entre 2 e 30 caracteres")
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s&]+$", message = "Categoria deve conter apenas letras, espaços e &")
    @Schema(description = "Categoria do produto", example = "Pizza", required = true,
            allowableValues = {"Pizza", "Hambúrguer", "Bebida", "Sobremesa", "Entrada", "Prato Principal", 
                              "Salada", "Sanduíche", "Massa", "Sushi", "Doce", "Lanche"})
    private String categoria;

    @NotNull(message = "Restaurante é obrigatório")
    @Positive(message = "ID do restaurante deve ser um número positivo")
    @Schema(description = "ID do restaurante", example = "1", required = true)
    private Long restauranteId;

    @Schema(description = "Disponibilidade do produto", example = "true")
    private Boolean disponivel = true;

    @Min(value = 0, message = "Tempo de preparo deve ser maior ou igual a 0")
    @Max(value = 180, message = "Tempo de preparo deve ser menor ou igual a 180 minutos")
    @Schema(description = "Tempo de preparo em minutos", example = "15")
    private Integer tempoPreparo;

    @DecimalMin(value = "0.0", message = "Peso deve ser maior ou igual a zero")
    @DecimalMax(value = "50.0", message = "Peso deve ser menor ou igual a 50kg")
    @Digits(integer = 2, fraction = 3, message = "Peso deve ter no máximo 2 dígitos inteiros e 3 decimais")
    @Schema(description = "Peso do produto em kg", example = "0.350")
    private BigDecimal peso;

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

    public Integer getTempoPreparo() {
        return tempoPreparo;
    }

    public void setTempoPreparo(Integer tempoPreparo) {
        this.tempoPreparo = tempoPreparo;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    // Métodos utilitários
    
    /**
     * Valida se o produto está em condições de ser vendido
     */
    public boolean isProdutoValido() {
        return nome != null && !nome.trim().isEmpty() &&
               descricao != null && descricao.length() >= 10 &&
               preco != null && preco.compareTo(BigDecimal.ZERO) > 0 &&
               categoria != null && !categoria.trim().isEmpty() &&
               restauranteId != null && restauranteId > 0;
    }

    /**
     * Normaliza os dados do produto
     */
    public void normalizarDados() {
        if (nome != null) {
            nome = nome.trim();
        }
        if (descricao != null) {
            descricao = descricao.trim();
        }
        if (categoria != null) {
            categoria = categoria.trim();
        }
        if (disponivel == null) {
            disponivel = true;
        }
    }

    /**
     * Calcula o preço por kg se o peso estiver informado
     */
    public BigDecimal calcularPrecoPorKg() {
        if (peso != null && peso.compareTo(BigDecimal.ZERO) > 0 && preco != null) {
            return preco.divide(peso, 2, java.math.RoundingMode.HALF_UP);
        }
        return null;
    }

    @Override
    public String toString() {
        return "ProdutoDTO{" +
                "nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", preco=" + preco +
                ", categoria='" + categoria + '\'' +
                ", restauranteId=" + restauranteId +
                ", disponivel=" + disponivel +
                ", tempoPreparo=" + tempoPreparo +
                ", peso=" + peso +
                '}';
    }
}