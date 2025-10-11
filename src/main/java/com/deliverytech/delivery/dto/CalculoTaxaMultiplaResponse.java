package com.deliverytech.delivery.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Schema(description = "DTO de resposta para cálculo de taxa múltipla")
public class CalculoTaxaMultiplaResponse {

    @Schema(description = "CEP consultado", example = "01234-567")
    private String cep;

    @Schema(description = "Número de restaurantes consultados", example = "5")
    private Integer totalConsultados;

    @Schema(description = "Número de restaurantes disponíveis", example = "3")
    private Integer totalDisponiveis;

    @Schema(description = "Lista de taxas calculadas")
    private List<TaxaEntregaResponse> taxas;

    @Schema(description = "Mapa de taxas por ID do restaurante")
    private Map<Long, TaxaEntregaResponse> taxasPorRestaurante;

    @Schema(description = "Data e hora do cálculo", example = "2024-01-20T14:30:00")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime calculadoEm;

    @Schema(description = "Tempo de processamento em milissegundos", example = "150")
    private Long tempoProcessamento;

    @Schema(description = "Restaurante com menor taxa (se disponível)")
    private TaxaEntregaResponse menorTaxa;

    @Schema(description = "Restaurante com maior taxa (se disponível)")
    private TaxaEntregaResponse maiorTaxa;

    // Constructors
    public CalculoTaxaMultiplaResponse() {
        this.calculadoEm = LocalDateTime.now();
    }

    public CalculoTaxaMultiplaResponse(String cep, Map<Long, TaxaEntregaResponse> taxasPorRestaurante) {
        this();
        this.cep = cep;
        this.taxasPorRestaurante = taxasPorRestaurante;
        this.totalConsultados = taxasPorRestaurante.size();
        this.totalDisponiveis = (int) taxasPorRestaurante.values().stream()
                .filter(TaxaEntregaResponse::isEntregaDisponivel)
                .count();
        
        // Converter para lista
        this.taxas = taxasPorRestaurante.values().stream()
                .filter(TaxaEntregaResponse::isEntregaDisponivel)
                .sorted((a, b) -> a.getTaxaEntrega().compareTo(b.getTaxaEntrega()))
                .toList();
        
        // Definir menor e maior taxa
        if (!taxas.isEmpty()) {
            this.menorTaxa = taxas.get(0);
            this.maiorTaxa = taxas.get(taxas.size() - 1);
        }
    }

    // Getters and Setters
    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public Integer getTotalConsultados() {
        return totalConsultados;
    }

    public void setTotalConsultados(Integer totalConsultados) {
        this.totalConsultados = totalConsultados;
    }

    public Integer getTotalDisponiveis() {
        return totalDisponiveis;
    }

    public void setTotalDisponiveis(Integer totalDisponiveis) {
        this.totalDisponiveis = totalDisponiveis;
    }

    public List<TaxaEntregaResponse> getTaxas() {
        return taxas;
    }

    public void setTaxas(List<TaxaEntregaResponse> taxas) {
        this.taxas = taxas;
    }

    public Map<Long, TaxaEntregaResponse> getTaxasPorRestaurante() {
        return taxasPorRestaurante;
    }

    public void setTaxasPorRestaurante(Map<Long, TaxaEntregaResponse> taxasPorRestaurante) {
        this.taxasPorRestaurante = taxasPorRestaurante;
    }

    public LocalDateTime getCalculadoEm() {
        return calculadoEm;
    }

    public void setCalculadoEm(LocalDateTime calculadoEm) {
        this.calculadoEm = calculadoEm;
    }

    public Long getTempoProcessamento() {
        return tempoProcessamento;
    }

    public void setTempoProcessamento(Long tempoProcessamento) {
        this.tempoProcessamento = tempoProcessamento;
    }

    public TaxaEntregaResponse getMenorTaxa() {
        return menorTaxa;
    }

    public void setMenorTaxa(TaxaEntregaResponse menorTaxa) {
        this.menorTaxa = menorTaxa;
    }

    public TaxaEntregaResponse getMaiorTaxa() {
        return maiorTaxa;
    }

    public void setMaiorTaxa(TaxaEntregaResponse maiorTaxa) {
        this.maiorTaxa = maiorTaxa;
    }

    // Métodos utilitários

    /**
     * Verifica se há restaurantes disponíveis
     */
    public boolean temRestaurantesDisponiveis() {
        return totalDisponiveis != null && totalDisponiveis > 0;
    }

    /**
     * Calcula a economia máxima possível
     */
    public String getEconomiaMaxima() {
        if (menorTaxa == null || maiorTaxa == null || 
            menorTaxa.getTaxaEntrega().equals(maiorTaxa.getTaxaEntrega())) {
            return "R$ 0,00";
        }
        
        double economia = maiorTaxa.getTaxaEntrega().doubleValue() - 
                         menorTaxa.getTaxaEntrega().doubleValue();
        
        return String.format("R$ %.2f", economia).replace(".", ",");
    }

    /**
     * Retorna resumo da consulta
     */
    public String getResumo() {
        if (!temRestaurantesDisponiveis()) {
            return String.format("Nenhum restaurante disponível para o CEP %s", cep);
        }
        
        return String.format("%d de %d restaurantes disponíveis para %s. " +
                           "Taxa varia de %s a %s (economia de até %s)",
                           totalDisponiveis, totalConsultados, cep,
                           menorTaxa.getTaxaEntregaFormatada(),
                           maiorTaxa.getTaxaEntregaFormatada(),
                           getEconomiaMaxima());
    }

    /**
     * Retorna lista de restaurantes ordenada por taxa
     */
    public List<TaxaEntregaResponse> getTaxasOrdenadas() {
        if (taxas == null) return List.of();
        
        return taxas.stream()
                .sorted((a, b) -> a.getTaxaEntrega().compareTo(b.getTaxaEntrega()))
                .toList();
    }

    /**
     * Retorna apenas restaurantes com entrega rápida (< 45 min)
     */
    public List<TaxaEntregaResponse> getEntregasRapidas() {
        if (taxas == null) return List.of();
        
        return taxas.stream()
                .filter(TaxaEntregaResponse::isEntregaRapida)
                .toList();
    }

    @Override
    public String toString() {
        return "CalculoTaxaMultiplaResponse{" +
                "cep='" + cep + '\'' +
                ", totalConsultados=" + totalConsultados +
                ", totalDisponiveis=" + totalDisponiveis +
                ", calculadoEm=" + calculadoEm +
                ", tempoProcessamento=" + tempoProcessamento +
                '}';
    }
}