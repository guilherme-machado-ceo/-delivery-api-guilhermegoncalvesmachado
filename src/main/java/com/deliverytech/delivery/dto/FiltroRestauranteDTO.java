package com.deliverytech.delivery.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "DTO para filtros avançados de busca de restaurantes")
public class FiltroRestauranteDTO {

    @Schema(description = "Nome do restaurante (busca parcial)", example = "Pizza")
    private String nome;

    @Schema(description = "Categoria do restaurante", example = "Pizzaria")
    private String categoria;

    @Schema(description = "Endereço do restaurante (busca parcial)", example = "Centro")
    private String endereco;

    @Schema(description = "Status ativo do restaurante", example = "true")
    private Boolean ativo;

    @Schema(description = "Avaliação mínima", example = "4.0", minimum = "1.0", maximum = "5.0")
    private Double avaliacaoMinima;

    @Schema(description = "Taxa máxima de entrega", example = "10.00")
    private BigDecimal taxaMaxima;

    @Schema(description = "Critério de ordenação", 
            example = "nome_asc",
            allowableValues = {"nome_asc", "nome_desc", "avaliacao_asc", "avaliacao_desc", 
                              "taxa_asc", "taxa_desc"})
    private String ordenacao;

    @Schema(description = "Número da página (para paginação)", example = "0")
    private Integer pagina;

    @Schema(description = "Tamanho da página (para paginação)", example = "20")
    private Integer tamanho;

    // Constructors
    public FiltroRestauranteDTO() {}

    public FiltroRestauranteDTO(String nome, String categoria, Boolean ativo) {
        this.nome = nome;
        this.categoria = categoria;
        this.ativo = ativo;
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

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Double getAvaliacaoMinima() {
        return avaliacaoMinima;
    }

    public void setAvaliacaoMinima(Double avaliacaoMinima) {
        this.avaliacaoMinima = avaliacaoMinima;
    }

    public BigDecimal getTaxaMaxima() {
        return taxaMaxima;
    }

    public void setTaxaMaxima(BigDecimal taxaMaxima) {
        this.taxaMaxima = taxaMaxima;
    }

    public String getOrdenacao() {
        return ordenacao;
    }

    public void setOrdenacao(String ordenacao) {
        this.ordenacao = ordenacao;
    }

    public Integer getPagina() {
        return pagina;
    }

    public void setPagina(Integer pagina) {
        this.pagina = pagina;
    }

    public Integer getTamanho() {
        return tamanho;
    }

    public void setTamanho(Integer tamanho) {
        this.tamanho = tamanho;
    }

    // Métodos utilitários

    /**
     * Verifica se há algum filtro ativo
     */
    public boolean temFiltros() {
        return (nome != null && !nome.trim().isEmpty()) ||
               (categoria != null && !categoria.trim().isEmpty()) ||
               (endereco != null && !endereco.trim().isEmpty()) ||
               ativo != null ||
               avaliacaoMinima != null ||
               taxaMaxima != null;
    }

    /**
     * Verifica se é uma busca por texto
     */
    public boolean isBuscaTexto() {
        return (nome != null && !nome.trim().isEmpty()) ||
               (endereco != null && !endereco.trim().isEmpty());
    }

    /**
     * Retorna o texto de busca combinado
     */
    public String getTextoBusca() {
        StringBuilder texto = new StringBuilder();
        
        if (nome != null && !nome.trim().isEmpty()) {
            texto.append(nome.trim());
        }
        
        if (endereco != null && !endereco.trim().isEmpty()) {
            if (texto.length() > 0) texto.append(" ");
            texto.append(endereco.trim());
        }
        
        return texto.toString();
    }

    /**
     * Normaliza os valores dos filtros
     */
    public void normalizar() {
        if (nome != null) {
            nome = nome.trim();
            if (nome.isEmpty()) nome = null;
        }
        
        if (categoria != null) {
            categoria = categoria.trim();
            if (categoria.isEmpty()) categoria = null;
        }
        
        if (endereco != null) {
            endereco = endereco.trim();
            if (endereco.isEmpty()) endereco = null;
        }
        
        if (ordenacao != null) {
            ordenacao = ordenacao.trim().toLowerCase();
            if (ordenacao.isEmpty()) ordenacao = null;
        }
        
        // Valores padrão para paginação
        if (pagina == null || pagina < 0) {
            pagina = 0;
        }
        
        if (tamanho == null || tamanho <= 0) {
            tamanho = 20;
        } else if (tamanho > 100) {
            tamanho = 100; // Limite máximo
        }
    }

    /**
     * Cria um filtro básico para busca simples
     */
    public static FiltroRestauranteDTO buscarPor(String texto, Boolean ativo) {
        FiltroRestauranteDTO filtro = new FiltroRestauranteDTO();
        filtro.setNome(texto);
        filtro.setAtivo(ativo);
        return filtro;
    }

    /**
     * Cria um filtro para categoria específica
     */
    public static FiltroRestauranteDTO porCategoria(String categoria, Boolean ativo) {
        FiltroRestauranteDTO filtro = new FiltroRestauranteDTO();
        filtro.setCategoria(categoria);
        filtro.setAtivo(ativo);
        return filtro;
    }

    @Override
    public String toString() {
        return "FiltroRestauranteDTO{" +
                "nome='" + nome + '\'' +
                ", categoria='" + categoria + '\'' +
                ", endereco='" + endereco + '\'' +
                ", ativo=" + ativo +
                ", avaliacaoMinima=" + avaliacaoMinima +
                ", taxaMaxima=" + taxaMaxima +
                ", ordenacao='" + ordenacao + '\'' +
                ", pagina=" + pagina +
                ", tamanho=" + tamanho +
                '}';
    }
}