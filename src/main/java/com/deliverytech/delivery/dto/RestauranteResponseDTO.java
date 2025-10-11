package com.deliverytech.delivery.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "DTO de resposta com dados completos do restaurante")
public class RestauranteResponseDTO {

    @Schema(description = "ID único do restaurante", example = "1", required = true)
    private Long id;

    @Schema(description = "Nome do restaurante", example = "Pizzaria do João", required = true)
    private String nome;

    @Schema(description = "Categoria do restaurante", example = "Pizzaria", required = true)
    private String categoria;

    @Schema(description = "Endereço completo do restaurante", 
            example = "Rua das Pizzas, 456 - Centro - São Paulo/SP - CEP: 01234-567", required = true)
    private String endereco;

    @Schema(description = "Taxa de entrega em reais", example = "5.50", required = true)
    private BigDecimal taxaEntrega;

    @Schema(description = "Avaliação média do restaurante (1.0 a 5.0)", example = "4.5", 
            minimum = "1.0", maximum = "5.0", required = true)
    private Double avaliacao;

    @Schema(description = "Status ativo do restaurante", example = "true", required = true)
    private Boolean ativo;

    @Schema(description = "Data e hora de criação do restaurante", 
            example = "2024-01-15T10:30:00")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime criadoEm;

    @Schema(description = "Data e hora da última atualização", 
            example = "2024-01-20T14:45:30")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime atualizadoEm;

    // Constructors
    public RestauranteResponseDTO() {}

    public RestauranteResponseDTO(Long id, String nome, String categoria, String endereco, 
                                 BigDecimal taxaEntrega, Double avaliacao, Boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
        this.endereco = endereco;
        this.taxaEntrega = taxaEntrega;
        this.avaliacao = avaliacao;
        this.ativo = ativo;
    }

    public RestauranteResponseDTO(Long id, String nome, String categoria, String endereco, 
                                 BigDecimal taxaEntrega, Double avaliacao, Boolean ativo,
                                 LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
        this.endereco = endereco;
        this.taxaEntrega = taxaEntrega;
        this.avaliacao = avaliacao;
        this.ativo = ativo;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
    }

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

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public void setAtualizadoEm(LocalDateTime atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }

    // Métodos utilitários para compatibilidade com frontend

    /**
     * Retorna a taxa de entrega formatada como string para exibição
     */
    public String getTaxaEntregaFormatada() {
        if (taxaEntrega == null) return "R$ 0,00";
        return String.format("R$ %.2f", taxaEntrega.doubleValue()).replace(".", ",");
    }

    /**
     * Retorna a avaliação formatada com uma casa decimal
     */
    public String getAvaliacaoFormatada() {
        if (avaliacao == null) return "5,0";
        return String.format("%.1f", avaliacao).replace(".", ",");
    }

    /**
     * Retorna o status como string para exibição
     */
    public String getStatusTexto() {
        return ativo != null && ativo ? "Ativo" : "Inativo";
    }

    /**
     * Verifica se o restaurante está ativo
     */
    public boolean isAtivo() {
        return ativo != null && ativo;
    }

    /**
     * Verifica se o restaurante foi criado recentemente (últimas 24 horas)
     */
    public boolean isNovo() {
        if (criadoEm == null) return false;
        return criadoEm.isAfter(LocalDateTime.now().minusDays(1));
    }

    /**
     * Verifica se o restaurante foi atualizado recentemente (últimas 24 horas)
     */
    public boolean isAtualizadoRecentemente() {
        if (atualizadoEm == null) return false;
        return atualizadoEm.isAfter(LocalDateTime.now().minusDays(1));
    }

    /**
     * Retorna uma representação resumida do restaurante para logs
     */
    public String getResumo() {
        return String.format("%s (%s) - %s - %s", 
                nome, categoria, getStatusTexto(), getTaxaEntregaFormatada());
    }

    /**
     * Verifica se o restaurante tem avaliação alta (>= 4.0)
     */
    public boolean isAvaliacaoAlta() {
        return avaliacao != null && avaliacao >= 4.0;
    }

    /**
     * Retorna a categoria normalizada (primeira letra maiúscula)
     */
    public String getCategoriaNormalizada() {
        if (categoria == null || categoria.trim().isEmpty()) return "";
        
        String[] palavras = categoria.toLowerCase().trim().split("\\s+");
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
        return resultado.toString();
    }

    @Override
    public String toString() {
        return "RestauranteResponseDTO{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", categoria='" + categoria + '\'' +
                ", endereco='" + endereco + '\'' +
                ", taxaEntrega=" + taxaEntrega +
                ", avaliacao=" + avaliacao +
                ", ativo=" + ativo +
                ", criadoEm=" + criadoEm +
                ", atualizadoEm=" + atualizadoEm +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        RestauranteResponseDTO that = (RestauranteResponseDTO) o;
        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}