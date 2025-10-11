package com.deliverytech.delivery.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Schema(description = "DTO para criação e atualização de restaurante")
public class RestauranteDTO {

    @NotBlank(message = "Nome do restaurante é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    @Schema(description = "Nome do restaurante", example = "Pizzaria do João", required = true)
    private String nome;

    @NotBlank(message = "Categoria é obrigatória")
    @Size(min = 2, max = 50, message = "Categoria deve ter entre 2 e 50 caracteres")
    @Schema(description = "Categoria do restaurante", example = "Pizzaria", required = true, 
            allowableValues = {"Pizzaria", "Hamburgueria", "Japonesa", "Italiana", "Brasileira", 
                              "Mexicana", "Chinesa", "Árabe", "Vegetariana", "Doces & Sobremesas", 
                              "Lanches", "Saudável"})
    private String categoria;

    @NotBlank(message = "Endereço é obrigatório")
    @Size(min = 10, max = 255, message = "Endereço deve ter entre 10 e 255 caracteres")
    @Schema(description = "Endereço completo do restaurante", 
            example = "Rua das Pizzas, 456 - Centro - São Paulo/SP - CEP: 01234-567")
    private String endereco;

    @NotNull(message = "Taxa de entrega é obrigatória")
    @DecimalMin(value = "0.0", inclusive = true, message = "Taxa de entrega deve ser maior ou igual a zero")
    @DecimalMax(value = "999.99", message = "Taxa de entrega deve ser menor que R$ 1000,00")
    @Digits(integer = 3, fraction = 2, message = "Taxa de entrega deve ter no máximo 3 dígitos inteiros e 2 decimais")
    @Schema(description = "Taxa de entrega do restaurante em reais", example = "5.50", required = true)
    private BigDecimal taxaEntrega;

    @DecimalMin(value = "1.0", message = "Avaliação deve ser entre 1.0 e 5.0")
    @DecimalMax(value = "5.0", message = "Avaliação deve ser entre 1.0 e 5.0")
    @Digits(integer = 1, fraction = 1, message = "Avaliação deve ter no máximo 1 dígito decimal")
    @Schema(description = "Avaliação média do restaurante (1.0 a 5.0)", example = "4.5", 
            minimum = "1.0", maximum = "5.0")
    private Double avaliacao;

    // Constructors
    public RestauranteDTO() {}

    public RestauranteDTO(String nome, String categoria, String endereco, BigDecimal taxaEntrega) {
        this.nome = nome;
        this.categoria = categoria;
        this.endereco = endereco;
        this.taxaEntrega = taxaEntrega;
        this.avaliacao = 5.0; // Valor padrão para novos restaurantes
    }

    public RestauranteDTO(String nome, String categoria, String endereco, BigDecimal taxaEntrega, Double avaliacao) {
        this.nome = nome;
        this.categoria = categoria;
        this.endereco = endereco;
        this.taxaEntrega = taxaEntrega;
        this.avaliacao = avaliacao != null ? avaliacao : 5.0;
    }

    // Getters and Setters
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

    // Métodos utilitários para compatibilidade com frontend
    
    /**
     * Valida se a categoria está entre as categorias permitidas
     */
    public boolean isCategoriaValida() {
        if (categoria == null) return false;
        
        String[] categoriasPermitidas = {
            "Pizzaria", "Hamburgueria", "Japonesa", "Italiana", "Brasileira",
            "Mexicana", "Chinesa", "Árabe", "Vegetariana", "Doces & Sobremesas",
            "Lanches", "Saudável"
        };
        
        for (String cat : categoriasPermitidas) {
            if (cat.equalsIgnoreCase(categoria.trim())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Normaliza a categoria para o formato padrão
     */
    public void normalizarCategoria() {
        if (categoria != null) {
            categoria = categoria.trim();
            // Capitaliza primeira letra de cada palavra
            String[] palavras = categoria.toLowerCase().split("\\s+");
            StringBuilder resultado = new StringBuilder();
            
            for (int i = 0; i < palavras.length; i++) {
                if (i > 0) resultado.append(" ");
                if (palavras[i].length() > 0) {
                    resultado.append(Character.toUpperCase(palavras[i].charAt(0)));
                    if (palavras[i].length() > 1) {
                        resultado.append(palavras[i].substring(1));
                    }
                }
            }
            categoria = resultado.toString();
        }
    }

    /**
     * Define avaliação padrão se não foi informada
     */
    public void definirAvaliacaoPadrao() {
        if (avaliacao == null) {
            avaliacao = 5.0;
        }
    }

    /**
     * Valida se todos os campos obrigatórios estão preenchidos
     */
    public boolean isValido() {
        return nome != null && !nome.trim().isEmpty() &&
               categoria != null && !categoria.trim().isEmpty() &&
               endereco != null && !endereco.trim().isEmpty() &&
               taxaEntrega != null && taxaEntrega.compareTo(BigDecimal.ZERO) >= 0;
    }

    @Override
    public String toString() {
        return "RestauranteDTO{" +
                "nome='" + nome + '\'' +
                ", categoria='" + categoria + '\'' +
                ", endereco='" + endereco + '\'' +
                ", taxaEntrega=" + taxaEntrega +
                ", avaliacao=" + avaliacao +
                '}';
    }
}