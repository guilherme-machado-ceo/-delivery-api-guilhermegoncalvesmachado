package com.deliverytech.delivery.dto;

import com.deliverytech.delivery.validation.ValidCEP;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;

@Schema(description = "DTO para cálculo de taxa de entrega múltipla")
public class CalculoTaxaMultiplaDTO {

    @NotBlank(message = "CEP é obrigatório")
    @ValidCEP(message = "CEP deve estar no formato válido (12345-678)")
    @Schema(description = "CEP de destino", example = "01234-567", required = true)
    private String cep;

    @NotEmpty(message = "Lista de restaurantes não pode estar vazia")
    @Size(min = 1, max = 10, message = "Deve informar entre 1 e 10 restaurantes")
    @Schema(description = "Lista de IDs dos restaurantes", example = "[1, 2, 3]", required = true)
    private List<Long> restauranteIds;

    @Schema(description = "Incluir apenas restaurantes ativos", example = "true")
    private Boolean apenasAtivos = true;

    @Schema(description = "Ordenar resultado por taxa crescente", example = "true")
    private Boolean ordenarPorTaxa = false;

    // Constructors
    public CalculoTaxaMultiplaDTO() {}

    public CalculoTaxaMultiplaDTO(String cep, List<Long> restauranteIds) {
        this.cep = cep;
        this.restauranteIds = restauranteIds;
    }

    // Getters and Setters
    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public List<Long> getRestauranteIds() {
        return restauranteIds;
    }

    public void setRestauranteIds(List<Long> restauranteIds) {
        this.restauranteIds = restauranteIds;
    }

    public Boolean getApenasAtivos() {
        return apenasAtivos;
    }

    public void setApenasAtivos(Boolean apenasAtivos) {
        this.apenasAtivos = apenasAtivos;
    }

    public Boolean getOrdenarPorTaxa() {
        return ordenarPorTaxa;
    }

    public void setOrdenarPorTaxa(Boolean ordenarPorTaxa) {
        this.ordenarPorTaxa = ordenarPorTaxa;
    }

    // Métodos utilitários

    /**
     * Normaliza o CEP removendo caracteres especiais
     */
    public void normalizarCep() {
        if (cep != null) {
            String cepLimpo = cep.replaceAll("[^0-9]", "");
            if (cepLimpo.length() == 8) {
                cep = cepLimpo.substring(0, 5) + "-" + cepLimpo.substring(5);
            }
        }
    }

    /**
     * Valida se a requisição está completa
     */
    public boolean isValida() {
        return cep != null && !cep.trim().isEmpty() &&
               restauranteIds != null && !restauranteIds.isEmpty() &&
               restauranteIds.size() <= 10;
    }

    /**
     * Retorna array de IDs para facilitar uso
     */
    public Long[] getRestauranteIdsArray() {
        if (restauranteIds == null) {
            return new Long[0];
        }
        return restauranteIds.toArray(new Long[0]);
    }

    @Override
    public String toString() {
        return "CalculoTaxaMultiplaDTO{" +
                "cep='" + cep + '\'' +
                ", restauranteIds=" + restauranteIds +
                ", apenasAtivos=" + apenasAtivos +
                ", ordenarPorTaxa=" + ordenarPorTaxa +
                '}';
    }
}