package com.deliverytech.delivery.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "DTO de resposta para cálculo de taxa de entrega")
public class TaxaEntregaResponse {

    @Schema(description = "Taxa de entrega calculada em reais", example = "8.50", required = true)
    private BigDecimal taxaEntrega;

    @Schema(description = "Distância em quilômetros", example = "5.2", required = true)
    private Double distancia;

    @Schema(description = "Tempo estimado de entrega", example = "30-45 min", required = true)
    private String tempoEstimado;

    @Schema(description = "CEP de destino utilizado no cálculo", example = "01234-567")
    private String cepDestino;

    @Schema(description = "ID do restaurante", example = "1")
    private Long restauranteId;

    @Schema(description = "Nome do restaurante", example = "Pizzaria do João")
    private String restauranteNome;

    @Schema(description = "Taxa base do restaurante", example = "5.00")
    private BigDecimal taxaBase;

    @Schema(description = "Taxa adicional por distância", example = "3.50")
    private BigDecimal taxaDistancia;

    @Schema(description = "Data e hora do cálculo", example = "2024-01-20T14:30:00")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime calculadoEm;

    @Schema(description = "Indica se a entrega está disponível para o CEP", example = "true")
    private Boolean entregaDisponivel;

    @Schema(description = "Observações sobre o cálculo", example = "Taxa promocional aplicada")
    private String observacoes;

    // Constructors
    public TaxaEntregaResponse() {
        this.calculadoEm = LocalDateTime.now();
        this.entregaDisponivel = true;
    }

    public TaxaEntregaResponse(BigDecimal taxaEntrega, Double distancia, String tempoEstimado) {
        this();
        this.taxaEntrega = taxaEntrega;
        this.distancia = distancia;
        this.tempoEstimado = tempoEstimado;
    }

    public TaxaEntregaResponse(BigDecimal taxaEntrega, Double distancia, String tempoEstimado, 
                              String cepDestino, Long restauranteId, String restauranteNome) {
        this(taxaEntrega, distancia, tempoEstimado);
        this.cepDestino = cepDestino;
        this.restauranteId = restauranteId;
        this.restauranteNome = restauranteNome;
    }

    // Getters and Setters
    public BigDecimal getTaxaEntrega() {
        return taxaEntrega;
    }

    public void setTaxaEntrega(BigDecimal taxaEntrega) {
        this.taxaEntrega = taxaEntrega;
    }

    public Double getDistancia() {
        return distancia;
    }

    public void setDistancia(Double distancia) {
        this.distancia = distancia;
    }

    public String getTempoEstimado() {
        return tempoEstimado;
    }

    public void setTempoEstimado(String tempoEstimado) {
        this.tempoEstimado = tempoEstimado;
    }

    public String getCepDestino() {
        return cepDestino;
    }

    public void setCepDestino(String cepDestino) {
        this.cepDestino = cepDestino;
    }

    public Long getRestauranteId() {
        return restauranteId;
    }

    public void setRestauranteId(Long restauranteId) {
        this.restauranteId = restauranteId;
    }

    public String getRestauranteNome() {
        return restauranteNome;
    }

    public void setRestauranteNome(String restauranteNome) {
        this.restauranteNome = restauranteNome;
    }

    public BigDecimal getTaxaBase() {
        return taxaBase;
    }

    public void setTaxaBase(BigDecimal taxaBase) {
        this.taxaBase = taxaBase;
    }

    public BigDecimal getTaxaDistancia() {
        return taxaDistancia;
    }

    public void setTaxaDistancia(BigDecimal taxaDistancia) {
        this.taxaDistancia = taxaDistancia;
    }

    public LocalDateTime getCalculadoEm() {
        return calculadoEm;
    }

    public void setCalculadoEm(LocalDateTime calculadoEm) {
        this.calculadoEm = calculadoEm;
    }

    public Boolean getEntregaDisponivel() {
        return entregaDisponivel;
    }

    public void setEntregaDisponivel(Boolean entregaDisponivel) {
        this.entregaDisponivel = entregaDisponivel;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    // Métodos utilitários

    /**
     * Retorna a taxa de entrega formatada para exibição
     */
    public String getTaxaEntregaFormatada() {
        if (taxaEntrega == null) return "R$ 0,00";
        return String.format("R$ %.2f", taxaEntrega.doubleValue()).replace(".", ",");
    }

    /**
     * Retorna a distância formatada para exibição
     */
    public String getDistanciaFormatada() {
        if (distancia == null) return "0,0 km";
        return String.format("%.1f km", distancia).replace(".", ",");
    }

    /**
     * Verifica se a entrega está disponível
     */
    public boolean isEntregaDisponivel() {
        return entregaDisponivel != null && entregaDisponivel;
    }

    /**
     * Verifica se é uma entrega de longa distância (> 10km)
     */
    public boolean isLongaDistancia() {
        return distancia != null && distancia > 10.0;
    }

    /**
     * Verifica se é uma entrega rápida (< 30 min)
     */
    public boolean isEntregaRapida() {
        if (tempoEstimado == null) return false;
        // Extrai o primeiro número do tempo estimado
        String[] partes = tempoEstimado.split("-");
        if (partes.length > 0) {
            try {
                int minutos = Integer.parseInt(partes[0].replaceAll("[^0-9]", ""));
                return minutos < 30;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }

    /**
     * Calcula o percentual da taxa de distância sobre o total
     */
    public Double getPercentualTaxaDistancia() {
        if (taxaEntrega == null || taxaDistancia == null || 
            taxaEntrega.compareTo(BigDecimal.ZERO) == 0) {
            return 0.0;
        }
        return (taxaDistancia.doubleValue() / taxaEntrega.doubleValue()) * 100;
    }

    /**
     * Retorna um resumo do cálculo para logs
     */
    public String getResumoCalculo() {
        return String.format("Restaurante %d: %s -> CEP %s = %s (%s)", 
                restauranteId, restauranteNome, cepDestino, 
                getTaxaEntregaFormatada(), getDistanciaFormatada());
    }

    /**
     * Cria uma resposta de erro quando a entrega não está disponível
     */
    public static TaxaEntregaResponse criarIndisponivel(String cepDestino, Long restauranteId, 
                                                       String motivo) {
        TaxaEntregaResponse response = new TaxaEntregaResponse();
        response.setCepDestino(cepDestino);
        response.setRestauranteId(restauranteId);
        response.setEntregaDisponivel(false);
        response.setObservacoes(motivo);
        response.setTaxaEntrega(BigDecimal.ZERO);
        response.setDistancia(0.0);
        response.setTempoEstimado("Indisponível");
        return response;
    }

    /**
     * Cria uma resposta básica com os dados essenciais
     */
    public static TaxaEntregaResponse criar(BigDecimal taxa, Double distancia, 
                                          String tempo, String cep, Long restauranteId) {
        TaxaEntregaResponse response = new TaxaEntregaResponse(taxa, distancia, tempo);
        response.setCepDestino(cep);
        response.setRestauranteId(restauranteId);
        return response;
    }

    @Override
    public String toString() {
        return "TaxaEntregaResponse{" +
                "taxaEntrega=" + taxaEntrega +
                ", distancia=" + distancia +
                ", tempoEstimado='" + tempoEstimado + '\'' +
                ", cepDestino='" + cepDestino + '\'' +
                ", restauranteId=" + restauranteId +
                ", restauranteNome='" + restauranteNome + '\'' +
                ", entregaDisponivel=" + entregaDisponivel +
                ", calculadoEm=" + calculadoEm +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        TaxaEntregaResponse that = (TaxaEntregaResponse) o;
        return restauranteId != null ? restauranteId.equals(that.restauranteId) : that.restauranteId == null &&
               cepDestino != null ? cepDestino.equals(that.cepDestino) : that.cepDestino == null;
    }

    @Override
    public int hashCode() {
        int result = restauranteId != null ? restauranteId.hashCode() : 0;
        result = 31 * result + (cepDestino != null ? cepDestino.hashCode() : 0);
        return result;
    }
}