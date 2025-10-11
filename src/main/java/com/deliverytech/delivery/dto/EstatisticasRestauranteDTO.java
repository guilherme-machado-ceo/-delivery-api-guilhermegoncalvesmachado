package com.deliverytech.delivery.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO com estatísticas dos restaurantes")
public class EstatisticasRestauranteDTO {

    @Schema(description = "Total de restaurantes cadastrados", example = "150")
    private Long totalRestaurantes;

    @Schema(description = "Número de restaurantes ativos", example = "120")
    private Long restaurantesAtivos;

    @Schema(description = "Número de restaurantes inativos", example = "30")
    private Long restaurantesInativos;

    @Schema(description = "Número de categorias diferentes", example = "12")
    private Long totalCategorias;

    @Schema(description = "Avaliação média geral", example = "4.2")
    private Double avaliacaoMedia;

    @Schema(description = "Taxa de entrega média", example = "6.50")
    private Double taxaMedia;

    // Constructors
    public EstatisticasRestauranteDTO() {}

    public EstatisticasRestauranteDTO(Long totalRestaurantes, Long restaurantesAtivos, 
                                     Long restaurantesInativos, Long totalCategorias,
                                     Double avaliacaoMedia, Double taxaMedia) {
        this.totalRestaurantes = totalRestaurantes;
        this.restaurantesAtivos = restaurantesAtivos;
        this.restaurantesInativos = restaurantesInativos;
        this.totalCategorias = totalCategorias;
        this.avaliacaoMedia = avaliacaoMedia;
        this.taxaMedia = taxaMedia;
    }

    // Getters and Setters
    public Long getTotalRestaurantes() {
        return totalRestaurantes;
    }

    public void setTotalRestaurantes(Long totalRestaurantes) {
        this.totalRestaurantes = totalRestaurantes;
    }

    public Long getRestaurantesAtivos() {
        return restaurantesAtivos;
    }

    public void setRestaurantesAtivos(Long restaurantesAtivos) {
        this.restaurantesAtivos = restaurantesAtivos;
    }

    public Long getRestaurantesInativos() {
        return restaurantesInativos;
    }

    public void setRestaurantesInativos(Long restaurantesInativos) {
        this.restaurantesInativos = restaurantesInativos;
    }

    public Long getTotalCategorias() {
        return totalCategorias;
    }

    public void setTotalCategorias(Long totalCategorias) {
        this.totalCategorias = totalCategorias;
    }

    public Double getAvaliacaoMedia() {
        return avaliacaoMedia;
    }

    public void setAvaliacaoMedia(Double avaliacaoMedia) {
        this.avaliacaoMedia = avaliacaoMedia;
    }

    public Double getTaxaMedia() {
        return taxaMedia;
    }

    public void setTaxaMedia(Double taxaMedia) {
        this.taxaMedia = taxaMedia;
    }

    // Métodos utilitários

    /**
     * Calcula o percentual de restaurantes ativos
     */
    public Double getPercentualAtivos() {
        if (totalRestaurantes == null || totalRestaurantes == 0) {
            return 0.0;
        }
        return (restaurantesAtivos.doubleValue() / totalRestaurantes.doubleValue()) * 100;
    }

    /**
     * Calcula o percentual de restaurantes inativos
     */
    public Double getPercentualInativos() {
        if (totalRestaurantes == null || totalRestaurantes == 0) {
            return 0.0;
        }
        return (restaurantesInativos.doubleValue() / totalRestaurantes.doubleValue()) * 100;
    }

    /**
     * Retorna a avaliação média formatada
     */
    public String getAvaliacaoMediaFormatada() {
        if (avaliacaoMedia == null) return "0,0";
        return String.format("%.1f", avaliacaoMedia).replace(".", ",");
    }

    /**
     * Retorna a taxa média formatada
     */
    public String getTaxaMediaFormatada() {
        if (taxaMedia == null) return "R$ 0,00";
        return String.format("R$ %.2f", taxaMedia).replace(".", ",");
    }

    /**
     * Verifica se há dados suficientes para estatísticas
     */
    public boolean temDados() {
        return totalRestaurantes != null && totalRestaurantes > 0;
    }

    /**
     * Retorna um resumo textual das estatísticas
     */
    public String getResumo() {
        if (!temDados()) {
            return "Nenhum restaurante cadastrado";
        }
        
        return String.format("%d restaurantes (%d ativos, %d inativos) em %d categorias - " +
                           "Avaliação média: %s - Taxa média: %s",
                           totalRestaurantes, restaurantesAtivos, restaurantesInativos,
                           totalCategorias, getAvaliacaoMediaFormatada(), getTaxaMediaFormatada());
    }

    @Override
    public String toString() {
        return "EstatisticasRestauranteDTO{" +
                "totalRestaurantes=" + totalRestaurantes +
                ", restaurantesAtivos=" + restaurantesAtivos +
                ", restaurantesInativos=" + restaurantesInativos +
                ", totalCategorias=" + totalCategorias +
                ", avaliacaoMedia=" + avaliacaoMedia +
                ", taxaMedia=" + taxaMedia +
                '}';
    }
}