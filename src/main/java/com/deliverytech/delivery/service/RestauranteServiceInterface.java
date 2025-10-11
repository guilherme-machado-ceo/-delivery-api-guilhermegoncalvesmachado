package com.deliverytech.delivery.service;

import com.deliverytech.delivery.dto.RestauranteDTO;
import com.deliverytech.delivery.dto.RestauranteResponseDTO;
import com.deliverytech.delivery.dto.TaxaEntregaResponse;
import java.math.BigDecimal;
import java.util.List;

public interface RestauranteServiceInterface {
    
    /**
     * Cadastra um novo restaurante com validações completas
     */
    RestauranteResponseDTO cadastrarRestaurante(RestauranteDTO dto);
    
    /**
     * Busca restaurante por ID com tratamento de erro
     */
    RestauranteResponseDTO buscarRestaurantePorId(Long id);
    
    /**
     * Busca restaurantes por categoria
     */
    List<RestauranteResponseDTO> buscarRestaurantesPorCategoria(String categoria);
    
    /**
     * Busca apenas restaurantes disponíveis (ativos)
     */
    List<RestauranteResponseDTO> buscarRestaurantesDisponiveis();
    
    /**
     * Atualiza restaurante validando existência
     */
    RestauranteResponseDTO atualizarRestaurante(Long id, RestauranteDTO dto);
    
    /**
     * Calcula taxa de entrega baseada no CEP (método legado)
     */
    BigDecimal calcularTaxaEntrega(Long restauranteId, String cep);
    
    /**
     * Ativa/desativa restaurante (toggle status ativo)
     */
    RestauranteResponseDTO ativarDesativarRestaurante(Long id);
}